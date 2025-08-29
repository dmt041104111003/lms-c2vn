'use client';

import { useState, useEffect } from 'react';
import { User,isWithin24Hours } from '~/constants/users';
import { AdminHeader } from '~/components/admin/common/AdminHeader';
import { AdminStats } from '~/components/admin/common/AdminStats';
import { AdminFilters } from '~/components/admin/common/AdminFilters';
import { UserTable } from '~/components/admin/users/UserTable';
import { Pagination } from '~/components/ui/pagination';
import Modal from '~/components/admin/common/Modal';
import { useToastContext } from '~/components/toast-provider';
import { useQuery } from '@tanstack/react-query';
import AdminTableSkeleton from '~/components/admin/common/AdminTableSkeleton';
import NotFoundInline from '~/components/ui/not-found-inline';
import { useNotifications } from "~/hooks/useNotifications";
import { OnlineUsersResponse, WEBSOCKET_CONFIG } from '~/constants/online-users';

export function UsersPageClient() {
  const [searchTerm, setSearchTerm] = useState('');
  const [filterType, setFilterType] = useState<'all' | 'active' | 'inactive' | 'admin' | 'user'>('all');
  const [currentPage, setCurrentPage] = useState(1);
  const [showAddModal, setShowAddModal] = useState(false);
  const [newUserAddress, setNewUserAddress] = useState('');
  const [newUserName, setNewUserName] = useState('');
  const [currentUserAddress, setCurrentUserAddress] = useState<string | null>(null);
  const [currentUserRole, setCurrentUserRole] = useState<'USER' | 'ADMIN' | null>(null);
  const [editUser, setEditUser] = useState<User | null>(null);
  const ITEMS_PER_PAGE = 6;
  const [editUserName, setEditUserName] = useState('');
  const { showSuccess, showError } = useToastContext();
  
  useNotifications();

  const {
    data: queryData,
    isLoading: loading,
    refetch: fetchUsers,
  } = useQuery({
    queryKey: ['admin-users'],
    queryFn: async () => {
      const res = await fetch('/api/admin/users', { credentials: 'include' });
      if (!res.ok) throw new Error('Failed to fetch users');
      return res.json();
    }
  });

  const {
    data: onlineData,
    isLoading: onlineLoading,
  } = useQuery<OnlineUsersResponse>({
    queryKey: ['online-users'],
    queryFn: async () => {
      const res = await fetch('/api/admin/online-users', { credentials: 'include' });
      if (!res.ok) throw new Error('Failed to fetch online users');
      return res.json();
    },
    refetchInterval: WEBSOCKET_CONFIG.REFETCH_INTERVAL,
    refetchIntervalInBackground: true,
  });

  const users: User[] = queryData?.data || [];

  useEffect(() => {
    const session = window.sessionStorage.getItem('next-auth.session');
    if (session) {
      try {
        const parsed = JSON.parse(session);
        setCurrentUserAddress(parsed.user?.address || null);
        setCurrentUserRole(parsed.user?.role || null);
      } catch {}
    }
  }, []);

  const handleCreateUser = async (address: string, name: string) => {
    try {
      const res = await fetch('/api/admin/users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ address, name })
      });
      if (!res.ok) {
        showError('Failed to create user');
        return;
      }
      await fetchUsers();
      setShowAddModal(false);
      showSuccess('User created', 'User has been created successfully.');
    } catch {
      showError('Failed to create user');
    }
  };

  const handleDelete = async (userId: string) => {
    const user = users.find(u => u.id === userId);
    if (!user) return;
    try {
      const res = await fetch('/api/admin/users', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ address: user.address })
      });
      if (!res.ok) {
        showError('Failed to delete user');
        return;
      }
      await fetchUsers();
      showSuccess('User deleted', 'User has been deleted.');
    } catch {
      showError('Failed to delete user');
    }
  };

  const handleRoleChange = async (userId: string, newRole: 'ADMIN' | 'USER') => {
    const user = users.find(u => u.id === userId);
    if (!user) return;
    
    if (user.address === currentUserAddress && newRole === 'USER') {
      showError('Cannot demote yourself to user');
      return;
    }
    
    try {
      const res = await fetch('/api/admin/users', {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ 
          address: user.address, 
          promote: newRole === 'ADMIN' 
        })
      });
      
      if (!res.ok) {
        const errorData = await res.json();
        showError(errorData.message || 'Failed to update user role');
        return;
      }
      
      await fetchUsers();
      showSuccess('Role updated', `User role has been updated to ${newRole}`);
    } catch {
      showError('Failed to update user role');
    }
  };

  const handleEdit = (user: User) => {
    setEditUser(user);
    setEditUserName(user.name || '');
  };

  const handleUpdateUserName = async () => {
    if (!editUser) return;
    try {
      const res = await fetch('/api/admin/users', {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ address: editUser.address, name: editUserName })
      });
      if (!res.ok) {
        showError('Failed to update user name');
        return;
      }
      await fetchUsers();
      setEditUser(null);
      showSuccess('User updated', 'User name has been updated.');
    } catch {
      showError('Failed to update user name');
    }
  };

  const handleBanUser = async (userId: string, hours: number) => {
    const user = users.find(u => u.id === userId);
    if (!user) return;
    
    try {
      const res = await fetch('/api/admin/users', {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ 
          address: user.address, 
          ban: true,
          banHours: hours
        })
      });
      
      if (!res.ok) {
        const errorData = await res.json();
        showError(errorData.message || 'Failed to ban user');
        return;
      }
      
      await fetchUsers();
      showSuccess('User banned', `User has been banned for ${hours} hours`);
    } catch {
      showError('Failed to ban user');
    }
  };

  const handleUnbanUser = async (userId: string) => {
    const user = users.find(u => u.id === userId);
    if (!user) return;
    
    try {
      const res = await fetch('/api/admin/users', {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ 
          address: user.address, 
          unban: true
        })
      });
      
      if (!res.ok) {
        const errorData = await res.json();
        showError(errorData.message || 'Failed to unban user');
        return;
      }
      
      await fetchUsers();
      showSuccess('User unbanned', 'User has been unbanned successfully');
    } catch {
      showError('Failed to unban user');
    }
  };

  const filteredUsers = users.filter(user => {
    const matchesSearch = (user.name?.toLowerCase() || '').includes(searchTerm.toLowerCase()) ||
      (user.address?.toLowerCase() || '').includes(searchTerm.toLowerCase());
    let matchesFilter = true;
    switch (filterType) {
      case 'active':
        matchesFilter = user.status === 'active';
        break;
      case 'inactive':
        matchesFilter = user.status === 'inactive';
        break;
      case 'admin':
        matchesFilter = user.role === 'ADMIN';
        break;
      case 'user':
        matchesFilter = user.role === 'USER';
        break;
      default:
        matchesFilter = true;
    }
    return matchesSearch && matchesFilter;
  });

  // useEffect(() => {
  //   setCurrentPage(1);
  // }, [searchTerm, filterType]);

  const onlineEntries: Array<{ userId: string; lastSeen: string | number }> = Array.isArray(onlineData?.data?.authenticated)
    ? onlineData!.data!.authenticated as any
    : [];
  const onlineMap = new Map<string, number>();
  onlineEntries.forEach((u: any) => {
    const ts = typeof u.lastSeen === 'string' ? Date.parse(u.lastSeen) : Number(u.lastSeen) || Date.now();
    if (u.userId) onlineMap.set(u.userId, ts);
  });

  const sortedFilteredUsers = [...filteredUsers].sort((a, b) => {
    const aTs = onlineMap.get(a.id) || (a.address ? onlineMap.get(a.address) : 0) || (a.email ? onlineMap.get(a.email as any) : 0) || 0;
    const bTs = onlineMap.get(b.id) || (b.address ? onlineMap.get(b.address) : 0) || (b.email ? onlineMap.get(b.email as any) : 0) || 0;
    if (aTs !== bTs) return bTs - aTs;
    const aUpd = a.updatedAt ? new Date(a.updatedAt).getTime() : 0;
    const bUpd = b.updatedAt ? new Date(b.updatedAt).getTime() : 0;
    return bUpd - aUpd;
  });

  const totalPages = Math.ceil(sortedFilteredUsers.length / ITEMS_PER_PAGE);
  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const paginatedUsers = sortedFilteredUsers.slice(startIndex, startIndex + ITEMS_PER_PAGE);

  const stats = [
    { label: 'Total Users', value: users.length, color: 'default' as const },
    { label: 'Active Users', value: users.filter(u => !u.isBanned).length, color: 'green' as const },
    { label: 'Banned Users', value: users.filter(u => u.isBanned).length, color: 'red' as const },
    { label: 'Admins', value: users.filter(u => u.role === 'ADMIN').length, color: 'blue' as const },
    { label: 'New Users (24h)', value: users.filter(u => isWithin24Hours(u.createdAt)).length, color: 'blue' as const },
    { label: 'Online Now', value: onlineData?.data?.stats?.total || 0, color: 'green' as const },
  ];

  const filterOptions = [
    { value: 'all', label: 'All Users' },
    { value: 'active', label: 'Active' },
    { value: 'inactive', label: 'Inactive' },
    { value: 'admin', label: 'Admins' },
    { value: 'user', label: 'Users' },
  ];

  return (
    <div className="space-y-6">
      <AdminHeader
        title="Users Management"
        description="Manage user accounts and permissions"
        buttonText="Add New User"
        onAddClick={() => setShowAddModal(true)}
      />
      <Modal isOpen={showAddModal} onClose={() => { setShowAddModal(false); setNewUserAddress(''); setNewUserName(''); }} title="Add New User">
        <input
          className="w-full border rounded px-3 py-2 mb-2"
          placeholder="Wallet address"
          value={newUserAddress}
          onChange={e => setNewUserAddress(e.target.value)}
          autoFocus
        />
        <input
          className="w-full border rounded px-3 py-2 mb-4"
          placeholder="Name (optional)"
          value={newUserName}
          onChange={e => setNewUserName(e.target.value)}
        />
        <div className="flex justify-end gap-2">
          <button className="px-4 py-2 bg-gray-200 rounded" onClick={() => { setShowAddModal(false); setNewUserAddress(''); setNewUserName(''); }}>Cancel</button>
          <button
            className="px-4 py-2 bg-blue-600 text-white rounded"
            onClick={async () => {
              if (newUserAddress.trim()) {
                await handleCreateUser(newUserAddress.trim(), newUserName.trim());
                setShowAddModal(false);
                setNewUserAddress('');
                setNewUserName('');
              }
            }}
          >Add</button>
        </div>
      </Modal>
      <AdminStats stats={stats} />
      <AdminFilters
        searchTerm={searchTerm}
        filterType={filterType}
        searchPlaceholder="Search users by name or address..."
        filterOptions={filterOptions}
        onSearchChange={setSearchTerm}
        onFilterChange={(v: string) => setFilterType(v as typeof filterType)}
      />
      {loading ? (
        <AdminTableSkeleton columns={5} rows={5} />
      ) : filteredUsers.length === 0 ? (
        <NotFoundInline 
          onClearFilters={() => {
            setSearchTerm('');
            setFilterType('all');
          }}
        />
      ) : (
      <div className="bg-white rounded-lg shadow">
        <UserTable
          users={paginatedUsers}
          onEdit={handleEdit}
          onDelete={handleDelete}
          onRoleChange={handleRoleChange}
          onBanUser={handleBanUser}
          onUnbanUser={handleUnbanUser}
          currentUserAddress={currentUserAddress}
          currentUserRole={currentUserRole || undefined}
        />
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          totalItems={filteredUsers.length}
          itemsPerPage={ITEMS_PER_PAGE}
          onPageChange={setCurrentPage}
        />
      </div>
      )}
      <Modal isOpen={!!editUser} onClose={() => setEditUser(null)} title="Edit User Name">
        <input
          className="w-full border rounded px-3 py-2 mb-4"
          placeholder="User name"
          value={editUserName}
          onChange={e => setEditUserName(e.target.value)}
          autoFocus
        />
        <div className="flex justify-end gap-2">
          <button className="px-4 py-2 bg-gray-200 rounded" onClick={() => setEditUser(null)}>Cancel</button>
          <button
            className="px-4 py-2 bg-blue-600 text-white rounded"
            onClick={handleUpdateUserName}
            disabled={!editUserName.trim()}
          >Save</button>
        </div>
      </Modal>
    </div>
  );
}

export default UsersPageClient; 