import { create } from "zustand";
import { User } from "../models/User.model";

interface AuthState {
  token: string | null;
  user: User | null;
  setAuthData: (userData: User | null, authToken: string | null) => void;
}

const useAuthStore = create<AuthState>((set) => ({
  token: null,
  user: null,
  setAuthData: (userData: User | null, authToken: string | null) => {
    set({ token: authToken, user: userData });
  },
}));

export default useAuthStore;
