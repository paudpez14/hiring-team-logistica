"use client";
import React, { useEffect, useState } from "react";
import axios from "axios";
import { Button, ConfigProvider, Table, Tooltip } from "antd";
import moment from "moment"; // Importa moment
import {
  FileSearchOutlined,
  EditOutlined,
  DeleteOutlined,
} from "@ant-design/icons";
import { ProductResumeModel } from "@/src/models/ProductResume.model";
import useAuthStore from "@/src/states/AuthStore";
import { PageModel } from "@/src/models/Page.model";
import TaskBarInventory from "./TaskBarInventory";
import InventoryForm from "./InventoryForm";
import { current } from "@reduxjs/toolkit";
import { InventoryModel } from "@/src/models/Inventory.model";
import EditInventoryform from "./EditInventoryform";

const PAGE_SIZE = 10;

export default function InventoryTable() {
  const [pageInfo, setPageInfo] = useState<PageModel<ProductResumeModel>>({
    current: 1,
  });
  const token = useAuthStore((state) => state.token);
  const user = useAuthStore((state) => state.user);
  const [loading, setLoading] = useState(true); // Estado de carga
  const [selectedInventory, setSelectInventory] = useState<InventoryModel>({});
  const [isNewInventory, setIsNewInventory] = useState(false);
  const [isEditInventory, setIsEditInventory] = useState(false);
  const [availableNewInventory, setIsAvailableNewInventory] = useState(false);
  const [availableEditInventory, setIsAvailableEditInventory] = useState(false);
  const apiUrl = process.env.NEXT_PUBLIC_HOST_FUNIBER_BACKEND || "";
  const apiIventoryPath =
    process.env.NEXT_PUBLIC_API_PATH_INVENTORY_MANAGEMENT || "";
  const fetchData = async (page: number) => {
    try {
      setLoading(true); // Iniciar la carga de productos
      const response = await axios.get(
        `${apiUrl}${apiIventoryPath}list-inventory?page=${page}&size=${PAGE_SIZE}&type=act`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            Email: user?.email || "",
            "Content-Type": "application/json",
          },
        }
      );
      if (response.status === 200) {
        let { current, hasNext, hasPrevious, numPages, sizeData, results } =
          response.data.data;

        const mappedResults: ProductResumeModel[] = results.map(
          (result: ProductResumeModel) => ({
            id: result.id,
            code: result.code,
            name: result.name,
            category: {
              id: result.category !== undefined ? result.category.id:0,
              name: result.category !== undefined ? result.category.name:"",
              code:  result.category !== undefined ? result.category.code:"",
              isActive:  result.category !== undefined ? result.category.isActive:"",
            },
            inventory: result.inventory
              ? {
                  id: result.inventory.id,
                  stockQuantity: result.inventory.stockQuantity,
                  length: result.inventory.length,
                  width: result.inventory.width,
                  height: result.inventory.height,
                  createdAt:  result.inventory.createdAt !== undefined ? new Date(result.inventory.createdAt) : null,
                  updatedAt:
                    result.inventory.updatedAt !== undefined
                      ? new Date(result.inventory.updatedAt)
                      : null,
                  createdBy: result.inventory.createdBy,
                  updatedBy: result.inventory.updatedBy,
                  isActive: result.inventory.isActive,
                }
              : null,
          })
        );
        results = mappedResults;
        setPageInfo({
          current,
          hasNext,
          hasPrevious,
          numPages,
          sizeData,
          results,
        });
        setLoading(false); // Iniciar la carga de productos
      } else {
        setLoading(false); // Iniciar la carga de productos
        console.error("Error fetching data:", response.data);
      }
    } catch (error) {
      setLoading(false); // Iniciar la carga de productos
      console.error("Error fetching data:", error);
    }
  };
  const onEdit = (inventory: InventoryModel) => {
    setSelectInventory(inventory);
    setIsAvailableEditInventory(true);
  };
  useEffect(() => {
    if (isNewInventory) {
      fetchData(1);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isNewInventory]);
  useEffect(() => {
    fetchData(pageInfo.current); // Cargar productos al cambiar la página
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [pageInfo.current]); // Dependencia: solo se ejecuta cuando pageInfo.current cambia
  useEffect(()=>{
    if(isEditInventory){
      fetchData(pageInfo.current)
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  },[isEditInventory])
  const onAddInventory = () => {
    setIsNewInventory(false);
    setIsAvailableNewInventory(true);
  };
  return (
    <div>
      <TaskBarInventory onAddInventory={onAddInventory}></TaskBarInventory>
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
              title: "Nombre del Producto",
              dataIndex: "name",
            },
            {
              title: "Stock Quantity",
              dataIndex: ["inventory", "stockQuantity"],
            },
            {
              title: "Length",
              dataIndex: ["inventory", "length"],
            },
            {
              title: "Width",
              dataIndex: ["inventory", "width"],
            },
            {
              title: "Height",
              dataIndex: ["inventory", "height"],
            },
            {
              title: "Created At",
              dataIndex: ["inventory", "createdAt"],
              render: (createdAt) =>
                moment(createdAt).format("YYYY-MM-DD HH:mm:ss"), // Formatea la fecha usando moment
            },
            {
              title: "Updated At",
              dataIndex: ["inventory", "updatedAt"],
              render: (updatedAt) =>
                updatedAt
                  ? moment(updatedAt).format("YYYY-MM-DD HH:mm:ss")
                  : "",
            },
            {
              title: "Created By",
              dataIndex: ["inventory", "createdBy", "email"],
            },
            {
              title: "Updated By",
              dataIndex: ["inventory", "updatedBy", "email"],
            },
            {
              title: "Acciones",
              render: (_, record) => {
                return (
                  <div className="d-flex">
                    <Tooltip title={"Editar"}>
                      <Button
                        type="text"
                        onClick={() => onEdit(record.inventory !== undefined  ? record.inventory : {})}
                        icon={<EditOutlined />}
                        style={{ border: "none", background: "none" }}
                      />
                    </Tooltip>
                  </div>
                );
              },
            },
          ]}
          loading={loading} // Show loader while data is being fetched
          dataSource={pageInfo.results}
          pagination={{
            current: pageInfo?.current,
            total: pageInfo?.sizeData,
            onChange: (page) => setPageInfo({ ...pageInfo, current: page }),
          }}
          rowKey={"id"}


        />
      </ConfigProvider>
      <InventoryForm
        setIsNewInventory={setIsNewInventory}
        availableInventory={availableNewInventory}
        setIsAvailableInventory={setIsAvailableNewInventory}
      ></InventoryForm>
      <EditInventoryform
        inventory={selectedInventory}
        availabeEditing={availableEditInventory}
        setAvailableEditing={setIsAvailableEditInventory}
        setIsEditInventory={setIsEditInventory}
      ></EditInventoryform>
    </div>
  );
}
