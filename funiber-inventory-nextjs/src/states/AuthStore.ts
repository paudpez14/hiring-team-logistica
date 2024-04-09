import { create } from "zustand";
import { UserModel } from "../models/User.model";

interface AuthState {
  token: string | null;
  user: UserModel | null;
  setAuthData: (userData: UserModel | null, authToken: string | null) => void;
}

const useAuthStore = create<AuthState>((set) => ({
  token: null,
  user: null,
  setAuthData: (userData: UserModel | null, authToken: string | null) => {
    set({ token: authToken, user: userData });
  },
}));

export default useAuthStore;
