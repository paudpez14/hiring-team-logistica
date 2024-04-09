"use client";
import React, { useEffect, useState } from "react";
import axios from "axios";
import { Button, ConfigProvider, Table, Tooltip } from "antd";
import {
  FileSearchOutlined,
  EditOutlined,
  DeleteOutlined,
} from "@ant-design/icons";
import { CategoryModel } from "@/src/models/Category.model"; // Asegúrate de importar el modelo de Categoría correcto
import useAuthStore from "@/src/states/AuthStore";
import { PageModel } from "@/src/models/Page.model";
import TaskBarCategory from "./TaskBarCategory"; // Ajusta el nombre del componente de la barra de tareas si es diferente
import EditCategoryModal from "./EditCategoryModal";
import NewCategoryModal from "./NewCategoryModal";

const PAGE_SIZE = 10;

export default function CategoryTable() {
  const [pageInfo, setPageInfo] = useState<PageModel<CategoryModel>>({
    current: 1,
  });
  const token = useAuthStore((state) => state.token);
  const user = useAuthStore((state) => state.user);
  const [loading, setLoading] = useState(true);
  const [availableEditCategory, setAvailableEditCategory] = useState(false);
  const [completeEditCategory, setCompleteEditCategory] = useState(false);
  const [availableNewCategory, setAvailableEditNewCategory] = useState(false);
  const [completeNewCategory, setCompleteNewCategory] = useState(false);
  const [selectCategory, setSelectCategory] = useState<CategoryModel>({});
  const apiUrl = process.env.NEXT_PUBLIC_HOST_FUNIBER_BACKEND || "";
  const apiCategoryPath =
    process.env.NEXT_PUBLIC_API_PATH_INVENTORY_MANAGEMENT || "";

  useEffect(() => {
    fetchData(pageInfo.current);
  }, [pageInfo.current]);

  useEffect(() => {
    if (completeEditCategory) {
      fetchData(pageInfo.current);
    }
  }, [completeEditCategory]);
  useEffect(() => {
    if (completeNewCategory) {
      fetchData(pageInfo.current);
    }
  }, [completeNewCategory]);
  const fetchData = async (page: number) => {
    try {
      setLoading(true);
      const response = await axios.get(
        `${apiUrl}${apiCategoryPath}categories?page=${page}&size=${PAGE_SIZE}&type=ALL`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            Email: user?.email || "",
            "Content-Type": "application/json",
          },
        }
      );
      if (response.status === 200) {
        const { current, hasNext, hasPrevious, numPages, sizeData, results } =
          response.data.data;
        setPageInfo({
          current,
          hasNext,
          hasPrevious,
          numPages,
          sizeData,
          results,
        });
        setLoading(false);
      } else {
        setLoading(false);
        console.error("Error fetching data:", response.data);
      }
    } catch (error) {
      setLoading(false);
      console.error("Error fetching data:", error);
    }
  };
  const onEditCategory = (value: CategoryModel) => {
    setSelectCategory(value);
    setAvailableEditCategory(true);
    setCompleteEditCategory(false);
  };
  const handleNewCategory = () => {
    setAvailableEditNewCategory(true);
    setCompleteNewCategory(false);
  };
  return (
    <div>
      <TaskBarCategory handleNewCategory={handleNewCategory}></TaskBarCategory>
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
              title: "Nombre de la Categoría",
              dataIndex: "name",
            },
            {
              title: "Código",
              dataIndex: "code",
            },
            {
              title: "Estado",
              dataIndex: "isActive",
              render: (isActive: string) =>
                isActive === "Y" ? "Activo" : "Inactivo",
            },
            {
              title: "Action",
              key: "action",
              render: (_, record) => {
                return (
                  <div className="d-flex">
                    <Tooltip title={"Editar"}>
                      <Button
                        type="text"
                        onClick={() => onEditCategory(record)}
                        icon={<EditOutlined />}
                        style={{ border: "none", background: "none" }}
                      />
                    </Tooltip>
                  </div>
                );
              },
            },
          ]}
          loading={loading}
          dataSource={pageInfo.results}
          pagination={{
            current: pageInfo?.current,
            total: pageInfo?.sizeData,
            onChange: (page) => setPageInfo({ ...pageInfo, current: page }),
          }}
          rowKey={"id"}
        />
      </ConfigProvider>
      <EditCategoryModal
        availableEditCategory={availableEditCategory}
        setAvailableEditCategory={setAvailableEditCategory}
        category={selectCategory}
        setCompleteEditCategory={setCompleteEditCategory}
      ></EditCategoryModal>
      <NewCategoryModal
        availableNewCategory={availableNewCategory}
        setAvailableEditNewCategory={setAvailableEditNewCategory}
        setCompleteNewCategory={setCompleteNewCategory}
      ></NewCategoryModal>
    </div>
  );
}
