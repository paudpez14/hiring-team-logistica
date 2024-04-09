"use client";
import React, { use, useEffect, useState } from "react";
import SearchFormProducts from "./SearchFormProducts";
import ProductTable from "./ProductTable";
import TaskBarProduct from "./TaskBarProduct";
import HistoryInventoryinventory from "./HistoryInventoryProduct";
import { CategoryModel } from "@/src/models/Category.model";
import ModalAddFormProduct from "./ModalAddFormProduct";
const PAGE_SIZE = 10; // Tamaño de página para la paginación
interface EstructureProductsProps {
  categories: CategoryModel[];
}
const EstructureProducts = ({ categories }: EstructureProductsProps) => {
  const [isAddProduct, setIsAddProduct] = useState(false);
  const [isNewProduct, setIsNewProduct] = useState(false);
  const [isFilter, setIsFilter] = useState(false);
  
  const [isActiveFilter, setIsActiveFilter] = useState(false);
  const handleSearch = () => {
    setIsActiveFilter(true);
  };
  const handleCancelFilter = () => {
    setIsActiveFilter(false);
    setIsFilter(false);
  };
  const onAddProduct = () => {
    setIsAddProduct(true);
  };
  const onFilter = () => {
    setIsFilter(true);
  };
  useEffect(() => {
    if (isNewProduct) {
      setIsAddProduct(false);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isNewProduct]); // Dependencia: solo se ejecuta cuando isNewProduct cambia

  return (
    <div className="container mx-auto px-4">
      <TaskBarProduct onAddProduct={onAddProduct} onFilter={onFilter} />
      <>
        <ProductTable
          setIsFilter={setIsFilter}
          isActiveFilter={isActiveFilter}
          categories={categories}
          isNewProduct={isNewProduct}
        />
      </>

      {isFilter && (
        <SearchFormProducts
          onSearch={handleSearch}
          onClose={handleCancelFilter}
        />
      )}
      {isAddProduct && (
        <ModalAddFormProduct
          setIsNewProduct={setIsNewProduct}
          setIsAddProduct={setIsAddProduct}
          isAddProduct={isAddProduct}
          categories={categories}
        ></ModalAddFormProduct>
      )}
    </div>
  );
};

export default EstructureProducts;
