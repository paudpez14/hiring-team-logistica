import { ProductModel } from "@/src/models/Product.model";
import React, { useEffect, useState } from "react";
import EditFormProduct from "./EditFormProduct";
import DeleteConfirmationProduct from "./DeleteConfirmationProduct";
import Link from "next/link";
import axios from "axios";
import useAuthStore from "@/src/states/AuthStore";
import { ProductResumeModel } from "@/src/models/ProductResume.model";
import { BarLoader } from "react-spinners";
import { Button, ConfigProvider, Table, Tooltip, message } from "antd";
import {
  FileSearchOutlined,
  CheckCircleTwoTone,
  CloseCircleTwoTone,
  EditOutlined,
  DeleteOutlined,
} from "@ant-design/icons";
import { CategoryModel } from "@/src/models/Category.model";
import { ProductEditFormModel } from "@/src/models/ProductEditForm.model";
import { useSearchTermsProducts } from "@/src/states/SearchTermProducts";
import HistoryInventoryinventory from "./HistoryInventoryProduct";
const PAGE_SIZE = 10; // Tamaño de página para la paginación

type ProductProps = {
  categories: CategoryModel[];
  isNewProduct: boolean;
  isActiveFilter: boolean;
  setIsFilter: (value: boolean) => void;
};

export default function ProductTable({
  categories,
  isNewProduct,
  isActiveFilter,
  setIsFilter,
}: ProductProps) {
  const apiUrl = process.env.NEXT_PUBLIC_HOST_FUNIBER_BACKEND || "";
  const apiIventoryPath =
    process.env.NEXT_PUBLIC_API_PATH_INVENTORY_MANAGEMENT || "";
  const token = useAuthStore((state) => state.token);
  const user = useAuthStore((state) => state.user);
  const categoryName = useSearchTermsProducts((state) => state.categoryName);
  const productCode = useSearchTermsProducts((state) => state.productCode);
  const productName = useSearchTermsProducts((state) => state.productName);
  const [selectedProduct, setSelectedProduct] = useState<ProductResumeModel>({
    name: "",
    category: {
      id: 0,
      name: "",
      code: "",
      isActive: "",
    },
    id: 0,
    code: "",
    inventory: {
      id: 0,
      stockQuantity: 0,
      length: 0,
      width: 0,
      height: 0,
      createdAt: new Date(),
      updatedAt: new Date(),
      createdBy: {
        id: 0,
        email: "",
      },
      updatedBy: {
        id: 0,
        email: "",
      },
      isActive: "",
    },
  });
  const [showHistoryInventory, setshowHistoryInventory] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [isDelete, setIsDelete] = useState(false);
  const [products, setProducts] = useState<ProductResumeModel[]>([]);
  const [loading, setLoading] = useState(true); // Estado de carga
  const [pageInfo, setPageInfo] = useState({
    current: 1,
    hasNext: false,
    hasPrevious: false,
    numPages: 1,
    sizeData: 0,
  });
  const [messageApi, contextHolder] = message.useMessage();

  const onEdit = (product: ProductResumeModel) => {
    setSelectedProduct(product);
    setIsEditing(true);
  };

  const onDelete = (product: ProductResumeModel) => {
    setSelectedProduct(product);
    setIsDelete(true);
  };

  const handleCancel = () => {
    setIsEditing(false);
  };
  const handleEdit = async (editedProduct: ProductEditFormModel) => {
    try {
      const body = {
        code: editedProduct.code,
        name: editedProduct.name,
        category_id: editedProduct.category_id,
      };
      const response = await axios.put(
        `${apiUrl}${apiIventoryPath}products/${editedProduct.id}`,
        body,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            Email: user?.email || "",
            "Content-Type": "application/json",
          },
        }
      );

      if (response.status === 200) {
        messageApi.open({
          type: "success",
          content: "Producto Editado",
          duration: 5,
        });
        setIsEditing(false);
        fetchProducts(pageInfo.current);
      } else {
        console.error("Error al editar el producto:", response.data);
        messageApi.open({
          type: "error",
          content: "Error al editar el producto",
          duration: 5,
        });
      }
    } catch (error) {
      messageApi.open({
        type: "error",
        content: "Error al editar el producto",
        duration: 5,
      });
      console.error("Error al editar el producto:", error);
    }
  };

  const fetchProducts = async (page: number) => {
    try {
      setLoading(true); // Iniciar la carga de productos
      const response = await axios.get(
        `${apiUrl}${apiIventoryPath}products-resume?page=${page}&size=${PAGE_SIZE}&type=act`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            Email: user?.email || "",
          },
        }
      );

      const { current, hasNext, hasPrevious, numPages, sizeData, results } =
        response.data.data;
      const mappedResults: ProductResumeModel[] = results.map((result: any) => ({
        id: result.id,
        code: result.code,
        name: result.name,
        category: {
          id: result.category.id,
          name: result.category.name,
          code: result.category.code,
          isActive: result.category.isActive,
        },
        inventory: result.inventory
          ? {
              id: result.inventory.id,
              stockQuantity: result.inventory.stockQuantity,
              length: result.inventory.length,
              width: result.inventory.width,
              height: result.inventory.height,
              createdAt: new Date(result.inventory.createdAt),
              updatedAt: new Date(result.inventory.updatedAt),
              createdBy: result.inventory.createdBy,
              updatedBy: result.inventory.updatedBy,
              isActive: result.inventory.isActive,
            }
          : null,
      }));
      setProducts(mappedResults);
      setPageInfo({ current, hasNext, hasPrevious, numPages, sizeData });
      setLoading(false); // Finalizar la carga de productos
    } catch (error) {
      console.error("Error al cargar productos:", error);
      setLoading(false); // Finalizar la carga en caso de error
    }
  };
  const searchProducts = async (searchParams: string) => {
    try {
      setLoading(true); // Iniciar la carga de productos
      const response = await axios.get(
        `${apiUrl}${apiIventoryPath}products/search${searchParams}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            Email: user?.email || "",
          },
        }
      );

      const { current, hasNext, hasPrevious, numPages, sizeData, results } =
        response.data.data;
      const mappedResults: ProductResumeModel[] = results.map((result: any) => ({
        id: result.id,
        code: result.code,
        name: result.name,
        category: {
          id: result.category.id,
          name: result.category.name,
          code: result.category.code,
          isActive: result.category.isActive,
        },
        inventory: result.inventory
          ? {
              id: result.inventory.id,
              stockQuantity: result.inventory.stockQuantity,
              length: result.inventory.length,
              width: result.inventory.width,
              height: result.inventory.height,
              createdAt: new Date(result.inventory.createdAt),
              updatedAt: new Date(result.inventory.updatedAt),
              createdBy: result.inventory.createdBy,
              updatedBy: result.inventory.updatedBy,
              isActive: result.inventory.isActive,
            }
          : null,
      }));
      setProducts(mappedResults);
      setPageInfo({ current, hasNext, hasPrevious, numPages, sizeData });
      setLoading(false); // Finalizar la carga de productos
      setIsFilter(false);
    } catch (error) {
      console.error("Error al cargar productos:", error);
      setLoading(false); // Finalizar la carga en caso de error
    }
  };

  useEffect(() => {
    fetchProducts(pageInfo.current); // Cargar productos al cambiar la página
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [pageInfo.current]); // Dependencia: solo se ejecuta cuando pageInfo.current cambia
  useEffect(() => {
    if (isActiveFilter) {
      const searchParams = `?code=${productCode}&name=${productName}&category=${categoryName}&page=${pageInfo.current}&size=${PAGE_SIZE}`;
      searchProducts(searchParams);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isActiveFilter]); // Dependencia: solo se ejecuta cuando isNewProduct cambia
  useEffect(() => {
    if (isNewProduct) {
      fetchProducts(pageInfo.current); // Cargar productos al cambiar isNewProduct a true
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isNewProduct]); // Dependencia: solo se ejecuta cuando isNewProduct cambia
  const handleConfirmDeleteProduct = async (product: ProductModel) => {
    try {
      const response = await axios.delete(
        `${apiUrl}${apiIventoryPath}products/${product.id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            Email: user?.email,
          },
        }
      );

      if (response.status === 200) {
        fetchProducts(pageInfo.current); // Volver a cargar la lista de productos
      }
    } catch (error) {
      console.error("Error al eliminar el producto:", error);
    } finally {
      setIsDelete(false);
    }
  };

  const showOnHistory = (product: ProductResumeModel) => {
    setSelectedProduct(product);
    setshowHistoryInventory(true);
  };

  return (
    <>
      <>
        {contextHolder}
        <ConfigProvider
          theme={{
            components: {
              Table: {
                headerBg: "#374151",
                headerColor: "#FFFFFF",
              },
            },
          }}
        >
          <Table
            columns={[
              {
                title: "Código del Producto",
                dataIndex: "code",
              },
              {
                title: "Nombre del Producto",
                dataIndex: "name",
              },
              {
                title: "Categoría",
                dataIndex: ["category", "name"],
              },
              {
                title: "Acciones",
                render: (_, record) => {
                  return (
                    <div className="d-flex">
                      <Tooltip title={"Historial"}>
                        <Button
                          type="text"
                          icon={<FileSearchOutlined />}
                          style={{ border: "none", background: "none" }}
                          onClick={() => showOnHistory(record)}
                        />
                      </Tooltip>
                      <Tooltip title={"Editar"}>
                        <Button
                          type="text"
                          icon={<EditOutlined />}
                          style={{ border: "none", background: "none" }}
                          onClick={() => onEdit(record)}
                        />
                      </Tooltip>
                      <Tooltip title={"Eliminar"} placement="topRight">
                        <Button
                          type="text"
                          icon={<DeleteOutlined />}
                          style={{ border: "none", background: "none" }}
                          onClick={() => onDelete(record)}
                        />
                      </Tooltip>
                    </div>
                  );
                },
              },
            ]}
            loading={loading}
            pagination={{
              current: pageInfo?.current,
              total: pageInfo?.sizeData,
              onChange: (page) => setPageInfo({ ...pageInfo, current: page }),
            }}
            dataSource={products}
            rowKey={"id"}
          />
        </ConfigProvider>
      </>
      {isEditing && (
        <EditFormProduct
          setIsEditing={setIsEditing}
          isEditing={isEditing}
          categories={categories}
          product={selectedProduct}
          onEdit={handleEdit}
          onCancel={handleCancel}
        />
      )}

      <DeleteConfirmationProduct
        product={selectedProduct}
        open={isDelete}
        onClose={() => setIsDelete(false)}
        onConfirm={handleConfirmDeleteProduct}
      />
      {showHistoryInventory && (
        <HistoryInventoryinventory
          idProduct={selectedProduct.id!}
          setshowHistoryInventory={setshowHistoryInventory}
          showHistoryInventory={showHistoryInventory}
        />
      )}
    </>
  );
}
