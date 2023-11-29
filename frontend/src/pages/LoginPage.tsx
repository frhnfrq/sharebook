import React, { useState } from "react";
import { useAuthBase } from "../AuthProvider";
import axios from "../network/axios";
import { Navigate } from "react-router-dom";
import { CircularProgress } from "@mui/joy";
import { ApiResponse } from "../types";

const LoginPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const authState = useAuthBase();

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    console.log("Login with:", email, password);
    const { data } = await axios.post("login", { email, password });
    const loginResponse: ApiResponse<string> = data;

    if (loginResponse.success) {
      authState.login(loginResponse.data!);
    } else {
      alert(data.message);
    }

    setEmail("");
    setPassword("");
  };

  if (authState.loading) {
    return (
      <>
        <div className="flex items-center justify-center h-screen">
          <CircularProgress />
        </div>
      </>
    );
  } else if (authState.user) {
    return (
      <>
        <Navigate to={"/home"} />
      </>
    );
  }

  return (
    <div className="flex justify-center items-center h-screen w-auto">
      <form
        className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4"
        onSubmit={handleLogin}
      >
        <div className="mb-4">
          <label
            className="block text-gray-700 text-sm font-bold mb-2"
            htmlFor="email"
          >
            Email
          </label>
          <input
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="email"
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="mb-6">
          <label
            className="block text-gray-700 text-sm font-bold mb-2"
            htmlFor="password"
          >
            Password
          </label>
          <input
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="password"
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <div className="flex items-center justify-between">
          <button
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            type="submit"
          >
            Log In
          </button>
        </div>
      </form>
    </div>
  );
};

export default LoginPage;
