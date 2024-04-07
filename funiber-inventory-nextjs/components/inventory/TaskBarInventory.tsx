import React from 'react'
interface TaskBarInventoryProps {
  onAddInventory: () => void;
}
export default function TaskBarInventory({onAddInventory}:TaskBarInventoryProps) {
  return (
    <div className="flex justify-between items-center text-white my-5">
      <button
        onClick={onAddInventory}
        className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
      >
        Añadir nuevo inventario
      </button>
    </div>
  );
}
