"use client";
import React from "react";
import Link from "next/link";
import Image from "next/image";
import { useParams } from "next/navigation";

type CategoryProps = {
  category: String;
};
export default function DashBoardSideBar({ category }: CategoryProps) {
  const params = useParams();

  const menuItems = [
    { label: "Inventario", path: "inventory" },
    { label: "Productos", path: "products" },
    { label: "Categorías", path: "categories" },
  ];
  return (
    <aside className=" sticky top-0 md:w-72 md:h-screen bg-gray-900 text-white flex flex-col">
      <div className="mb-5 cursor-pointer">
        <Link href={"/login"}>
          <Image
            src="https://www.funiber.org/themes/funiber/logos/es.svg"
            alt="Logo"
            width={500}
            height={300}
          />
        </Link>
      </div>
      <div className="mt-5 flex-grow overflow-auto">
        {menuItems.map((item, index) => (
          <Link key={index} href={`/dashboard/${item.path}`}>
            <div
              className={` ${
                item.path == params.category ? "bg-gray-700" : ""
              }  "hover:bg-gray-700 rounded cursor-pointer flex items-center gap-4 w-full border-t border-gray-200 p-3 last-of-type:border-b"`}
            >
              <p>{item.label}</p>
            </div>
          </Link>
        ))}
      </div>
      <div className="my-5 p-3 mx-5 bg-red-600 text-center rounded cursor-pointer">
        Cerrar sesión
      </div>
    </aside>
  );
}
