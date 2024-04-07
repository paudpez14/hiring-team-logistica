"use client";
import React from "react";
interface TaskBarCategoryProps {
    handleNewCategory: ()=> void;
}
export default function TaskBarCategory({handleNewCategory}: TaskBarCategoryProps) {
  return (
    <div className="flex justify-between items-center text-white my-5">
      <button onClick={handleNewCategory} className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
        Añadir nueva Categoria
      </button>
    </div>
  );
}
