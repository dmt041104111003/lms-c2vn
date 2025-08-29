"use client";

import React, { useState, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useToastContext } from '~/components/toast-provider';
import ContentSection from './ContentSection';
import MediaSection from './MediaSection';
import FormActions from './FormActions';

interface LandingContentManagerProps {
  formData: {
    section: string;
    title: string;
    subtitle: string;
    description: string;
    mainText: string;
    subText: string;
    media1Url: string;
    media2Url: string;
    media3Url: string;
    media4Url: string;
    publishStatus: 'DRAFT' | 'PUBLISHED';
  };
  setFormData: React.Dispatch<React.SetStateAction<{
    section: string;
    title: string;
    subtitle: string;
    description: string;
    mainText: string;
    subText: string;
    media1Url: string;
    media2Url: string;
    media3Url: string;
    media4Url: string;
    publishStatus: 'DRAFT' | 'PUBLISHED';
  }>>;
}

export default function LandingContentManager({ formData, setFormData }: LandingContentManagerProps) {
  const { showSuccess, showError } = useToastContext();
  const queryClient = useQueryClient();
  const [currentContentId, setCurrentContentId] = useState<string | null>(null);

  const { data: landingContents = [] } = useQuery({
    queryKey: ['landing-content'],
    queryFn: async () => {
      const response = await fetch('/api/admin/landing-content', {
        credentials: 'include'
      });
      if (!response.ok) {
        throw new Error('Failed to fetch landing content');
      }
      const data = await response.json();
      return data?.data || [];
    }
  });

  useEffect(() => {
    if (landingContents.length > 0) {
      const firstContent = landingContents[0];
      setCurrentContentId(firstContent.id);
      setFormData(prev => {
        if (prev.title === '' && prev.subtitle === '' && prev.description === '') {
          return {
            section: firstContent.section || 'hero',
            title: firstContent.title || '',
            subtitle: firstContent.subtitle || '',
            description: firstContent.description || '',
            mainText: firstContent.mainText || '',
            subText: firstContent.subText || '',
            media1Url: firstContent.media1Url || '',
            media2Url: firstContent.media2Url || '',
            media3Url: firstContent.media3Url || '',
            media4Url: firstContent.media4Url || '',
            publishStatus: firstContent.publishStatus || 'DRAFT'
          };
        }
        return prev;
      });
    } else {
      setCurrentContentId(null);
      setFormData(prev => {
        if (prev.title !== '' || prev.subtitle !== '' || prev.description !== '') {
          return {
            section: 'hero',
            title: '',
            subtitle: '',
            description: '',
            mainText: '',
            subText: '',
            media1Url: '',
            media2Url: '',
            media3Url: '',
            media4Url: '',
            publishStatus: 'DRAFT'
          };
        }
        return prev;
      });
    }
  }, [landingContents, setFormData]);

  const updateMutation = useMutation({
    mutationFn: async (data: {
      section: string;
      title?: string;
      subtitle?: string;
      description?: string;
      mainText?: string;
      subText?: string;
      media1Url?: string;
      media2Url?: string;
      media3Url?: string;
      media4Url?: string;
    }) => {
      const method = currentContentId ? 'PUT' : 'POST';
      const url = currentContentId 
        ? `/api/admin/landing-content/${currentContentId}`
        : '/api/admin/landing-content';
      
      const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(data)
      });
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.error || 'Failed to save landing content');
      }
      return response.json();
    },
    onSuccess: (data) => {
      showSuccess('Landing content saved successfully');
      if (!currentContentId && data?.data?.id) {
        setCurrentContentId(data.data.id);
      }
      queryClient.invalidateQueries({ queryKey: ['landing-content'] });
    },
    onError: (error: Error) => {
      showError(error.message);
    }
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    console.log('Form data before submit:', formData);
    
    const submitData = {
      section: formData.section,
      title: formData.title,
      subtitle: formData.subtitle,
      description: formData.description,
      mainText: formData.mainText,
      subText: formData.subText,
      publishStatus: formData.publishStatus,
    };

    if (formData.media1Url && formData.media1Url.trim()) {
      (submitData as any).media1Url = formData.media1Url;
    }
    if (formData.media2Url && formData.media2Url.trim()) {
      (submitData as any).media2Url = formData.media2Url;
    }
    if (formData.media3Url && formData.media3Url.trim()) {
      (submitData as any).media3Url = formData.media3Url;
    }
    if (formData.media4Url && formData.media4Url.trim()) {
      (submitData as any).media4Url = formData.media4Url;
    }

    console.log('Submit data:', submitData);
    updateMutation.mutate(submitData);
  };

  const handleMediaSelect = (media: { id: string; url: string; type: string }) => {
    console.log('Media selected:', media);
    
    const emptySlot = ['media1Url', 'media2Url', 'media3Url', 'media4Url'].find(
      field => !formData[field as keyof typeof formData] || formData[field as keyof typeof formData] === ''
    );
    
    if (emptySlot) {
      setFormData(prev => ({
        ...prev,
        [emptySlot]: media.url
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        media4Url: media.url
      }));
    }
  };

  const handleRemoveMedia = (index: number) => {
    const mediaField = `media${index + 1}Url` as keyof typeof formData;
    setFormData(prev => ({
      ...prev,
      [mediaField]: ''
    }));
  };

  return (
    <div className="space-y-6">
      <div className="border border-gray-200 dark:border-gray-700 rounded-lg p-6">
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="grid grid-cols-1 gap-8 xl:grid-cols-2">
            <ContentSection formData={formData} setFormData={setFormData} />
            <MediaSection 
              formData={formData} 
              setFormData={setFormData}
              handleMediaSelect={handleMediaSelect}
              handleRemoveMedia={handleRemoveMedia}
            />
          </div>
          
          <FormActions 
            isPending={updateMutation.isPending} 
            publishStatus={formData.publishStatus}
            onPublishStatusChange={(status) => setFormData(prev => ({ ...prev, publishStatus: status }))}
          />
        </form>
      </div>
    </div>
  );
} 