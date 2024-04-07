import EstructureInventory from "@/components/inventory/EstructureInventory";
import EstructureCategory from "@/components/category/EstructureCategory";
import EstructureProducts from "@/components/products/EstructureProducts";
import { Category } from "@/src/models/Category.model";
import { User } from "@/src/models/User.model";
import useAuthStore from "@/src/states/AuthStore";
import React from "react";
const apiUrl = process.env.NEXT_PUBLIC_HOST_FUNIBER_BACKEND || "";
const apiIventoryPath =
  process.env.NEXT_PUBLIC_API_PATH_INVENTORY_MANAGEMENT || "";

type CategoryPageProps = {
  params: Record<string, string> | null | undefined;
};

async function getCategories() {
  try {
    const response = await fetch(
      `${apiUrl}${apiIventoryPath}categories?page=1&size=99999&type=act`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    const data = await response.json();
    return data.data.results;
  } catch (error) {
    console.error("There was a problem with your fetch operation:", error);
    return [];
  }
}

export default async function CategoryPage({ params }: CategoryPageProps) {
  const categories: Category[] = await getCategories();

  let categoryComponent;
  if (params?.category === "products") {
    categoryComponent = <EstructureProducts categories={categories}></EstructureProducts>;
  } else if (params?.category === "inventory") {
    categoryComponent = <EstructureInventory></EstructureInventory>;
  } else if (params?.category === "categories") {
    categoryComponent = <EstructureCategory></EstructureCategory>;
  }
  return <div>{categoryComponent}</div>;
}
