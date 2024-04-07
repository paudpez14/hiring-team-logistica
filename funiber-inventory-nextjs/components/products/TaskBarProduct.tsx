import React from "react";

interface TaskBarProductProps {
  onFilter: () => void;
  onAddProduct: () => void;
}
export default function TaskBarProduct({
  onAddProduct,
  onFilter,
}: TaskBarProductProps) {
  return (
    <div className="flex justify-between items-center text-white my-5">
      <button
        onClick={onAddProduct}
        className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
      >
        Añadir nuevo producto
      </button>
      <button
        onClick={onFilter}
        className="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded"
      >
        Filtrar
      </button>
    </div>
  );
}
