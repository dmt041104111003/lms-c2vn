export interface StatsData {
  members: number;
  total: number;
}

export const STATS_CONFIG = {
  REFETCH_INTERVAL: 30000, // 30 seconds
  LOADING_TIMEOUT: 600, // 600ms
} as const;













