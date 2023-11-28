import React, { createContext, useContext, useState, useEffect } from "react";
import { ApiResponse, IProps, User } from "./types";
import axios from "./network/axios";

type AuthState = {
  user?: User;
  loading: boolean;
  logout: () => void;
  login: (token: string) => void;
};

const AuthContext = createContext<AuthState>({
  loading: false,
  logout() {},
  login(token) {},
});

export const AuthProvider = ({ children }: IProps) => {
  const [user, setUser] = useState<User | undefined>();
  const [loading, setLoading] = useState(true);

  const authenticateUser = async (token: string) => {
    try {
      setLoading(true);

      const { data } = await axios.get("user", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      const response: ApiResponse<User> = data;

      if (response.success) {
        setUser(response.data);
      }
    } catch (error) {
      console.log(error);
      setUser(undefined);
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.clear();
    setUser(undefined);
  };

  const login = (token: string) => {
    localStorage.setItem("token", token);
    authenticateUser(token);
  };

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (token) {
      authenticateUser(token);
    } else {
      setLoading(false);
    }
  }, []);

  const contextValues = {
    user,
    loading,
    logout,
    login,
  };

  return (
    <AuthContext.Provider value={contextValues}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuthBase = () => {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }

  return context;
};

type AuthWithNonNullUser = AuthState & { user: NonNullable<AuthState["user"]> };

export const useAuth = () => {
  const auth = useAuthBase() as AuthWithNonNullUser;

  return auth;
};
