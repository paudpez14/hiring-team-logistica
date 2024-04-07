"use client";
import useAuthStore from "@/src/states/AuthStore";
import axios from "axios";
import React, { useState } from "react";
import { useRouter } from "next/navigation";

export default function Signin() {
  const apiUrl = process.env.NEXT_PUBLIC_HOST_FUNIBER_BACKEND;
  const apiUserPath = process.env.NEXT_PUBLIC_API_PATH_USER_MANAGEMENT;
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const setAuthData = useAuthStore((state) => state.setAuthData);
  const router = useRouter();

  const handleLogin = async () => {
    if (!email || !password) {
      setError("Por favor, completa todos los campos.");
      setTimeout(() => {
        setError("");
      }, 5000);
      return;
    }

    try {
      const response = await axios.post(`${apiUrl}${apiUserPath}login`, {
        email,
        password,
      });
      const { user, token } = response.data.data; // Extraer el usuario y el token de la respuesta
      setAuthData(user, token); // Guardar el usuario y el token en zustand
      localStorage.setItem("user", JSON.stringify(user));
      localStorage.setItem("token", token);
      // Redirigir al dashboard
      router.push("/dashboard");
    } catch (error) {
      console.error("Error al iniciar sesión:", error);
      setError("Credenciales incorrectas. Por favor, intenta de nuevo.");
      setTimeout(() => {
        setError("");
      }, 5000);
    }
  };

  return (
    <>
      <div className="p-12 bg-white rounded shadow-xl w-96">
        <h1 className="text-3xl font-bold mb-6">Iniciar sesión</h1>
        {error && (
          <div className="absolute top-0   mt-4 mr-4 bg-red-500 text-white px-4 py-2 rounded-lg shadow-md transition-transform transform translate-x-full">
            {error}
          </div>
        )}
        <form>
          <div className="mb-5">
            <label className="block mb-2 text-sm font-bold text-gray-700">
              Correo electrónico
            </label>
            <input
              className="w-full px-3 py-2 text-sm leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
              type="email"
              placeholder="Correo electrónico"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div className="mb-5">
            <label className="block mb-2 text-sm font-bold text-gray-700">
              Contraseña
            </label>
            <input
              className="w-full px-3 py-2 mb-3 text-sm leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
              type="password"
              placeholder="Contraseña"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <button
            className="w-full px-4 py-2 font-bold text-white bg-blue-500 rounded-full hover:bg-blue-700 focus:outline-none focus:shadow-outline"
            type="button"
            onClick={handleLogin}
          >
            Iniciar sesión
          </button>
        </form>
      </div>
    </>
  );
}
