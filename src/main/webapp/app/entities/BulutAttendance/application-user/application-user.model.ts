export interface IApplicationUser {
  id: number;
  userId?: string | null;
}

export type NewApplicationUser = Omit<IApplicationUser, 'id'> & { id: null };
