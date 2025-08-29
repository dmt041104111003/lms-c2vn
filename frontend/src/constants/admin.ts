// AdminFilters interfaces
export interface AdminFiltersProps {
  searchTerm: string;
  filterType: string;
  searchPlaceholder: string;
  filterOptions: { value: string; label: string }[];
  onSearchChange: (value: string) => void;
  onFilterChange: (value: string) => void;
}

// AdminHeader interfaces
export interface AdminHeaderProps {
  title: string;
  description: string;
  buttonText: string;
  onAddClick?: () => void;
}

// AdminStats interfaces
export interface StatItem {
  label: string;
  value: number;
  color?: 'default' | 'green' | 'blue' | 'red';
}

export interface AdminStatsProps {
  stats: StatItem[];
}

// AdminTableSkeleton interfaces
export interface AdminTableSkeletonProps {
  columns: number;
  rows?: number;
}

// Modal interfaces
export interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title?: string;
  children: React.ReactNode;
  maxWidth?: string;
}



export interface LandingContentFormData {
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
}

export interface LandingContentProps {
  content: {
    title: string;
    subtitle: string;
    description: string;
    mainText: string;
    subText: string;
  };
}

export interface LandingMediaProps {
  mediaItems: string[];
}

// Welcome Modal interfaces
export interface WelcomeModalData {
  id?: string;
  title: string;
  description: string;
  imageUrl: string | null;
  buttonLink: string | null;
  startDate: string | null;
  endDate: string | null;
  publishStatus: 'DRAFT' | 'PUBLISHED';
  isActive: boolean;
} 