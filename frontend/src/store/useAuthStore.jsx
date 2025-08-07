// src/store/useAuthStore.js
import { create } from "zustand";

export const useAuthStore = create((set) => ({
  user: null,
  token: null,
  isLoadingAuth: true, 
  login: (userData, token) => {
    localStorage.setItem("user", JSON.stringify(userData));
    localStorage.setItem("token", token);
    set({ user: userData, token, isLoadingAuth: false });
  },
  logout: () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    set({ user: null, token: null, isLoadingAuth: false });
  },
  loadAuthFromStorage: () => {
    const storedUser = localStorage.getItem("user");
    const storedToken = localStorage.getItem("token");
    if (storedUser && storedToken) {
      set({ user: JSON.parse(storedUser), token: storedToken, isLoadingAuth: false });
    } else {
      set({ isLoadingAuth: false });
    }
  }
}));