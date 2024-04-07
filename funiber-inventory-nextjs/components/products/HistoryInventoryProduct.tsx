import { Inventory } from "@/src/models/Inventory.model";
import { Page } from "@/src/models/Page.model";
import { User } from "@/src/models/User.model";
import useAuthStore from "@/src/states/AuthStore";
import {
  Button,
  ConfigProvider,
  Drawer,
  DrawerProps,
  Table,
  Tooltip,
  message,
} from "antd";
import axios from "axios";
import moment from "moment";
import React, { Suspense, useEffect, useState } from "react";
import { BarLoader } from "react-spinners";
const PAGE_SIZE = 10; // Tamaño de página para la paginación
interface HistoryInventoryinventoryProps {
  showHistoryInventory: boolean;
  idProduct: number;
  setshowHistoryInventory: (value: boolean) => void;
}

export default function HistoryInventoryinventory({
  showHistoryInventory,
  idProduct,
  setshowHistoryInventory,
}: HistoryInventoryinventoryProps) {
  const apiUrl = process.env.NEXT_PUBLIC_HOST_FUNIBER_BACKEND || "";
  const apiIventoryPath =
    process.env.NEXT_PUBLIC_API_PATH_INVENTORY_MANAGEMENT || "";
  const token = useAuthStore((state) => state.token);
  const user = useAuthStore((state) => state.user);
  const [loading, setLoading] = useState(true);
  const [pageInfo, setPageInfo] = useState<Page<Inventory[]>>({ current: 1 });
  const [open, setOpen] = useState(showHistoryInventory);
  const [placement] = useState<DrawerProps["placement"]>("bottom");
  const [messageApi, contextHolder] = message.useMessage();
  const fetchHistoryInventory = async (page: number) => {
    try {
      setLoading(true);
      const response = await axios.get(
        `${apiUrl}${apiIventoryPath}historic-inventory/${idProduct}?page=${page}&size=${PAGE_SIZE}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            Email: user?.email || "",
          },
        }
      );
      let { current, hasNext, hasPrevious, numPages, sizeData, results } =
        response.data.data;

      const mappedResults: Inventory[] = results.map((result: Inventory) => ({
        id: result.id,
        stockQuantity: result.stockQuantity,
        length: result.length,
        width: result.width,
        height: result.height,
        createdAt: new Date(result.createdAt),
        updatedAt:
          result.updatedAt !== null ? new Date(result.updatedAt) : null,
        createdBy: result.createdBy,
        updatedBy: result.updatedBy,
        isActive: result.isActive,
      }));
      results = mappedResults;

      setPageInfo({
        current,
        hasNext,
        hasPrevious,
        numPages,
        sizeData,
        results,
      });
      setLoading(false); // Finalizar la carga de productos
    } catch (error) {
      console.error("Error al cargar productos:", error);
      setLoading(false); // Finalizar la carga en caso de error
    }
  };

  const onClose = () => {
    setOpen(false);
    setshowHistoryInventory(false);
  };

  useEffect(() => {
    if (showHistoryInventory) {
      fetchHistoryInventory(pageInfo.current);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [showHistoryInventory, pageInfo.current]);

  return (
    <>
      <Drawer
        title="Hitorico de Inventario"
        placement={placement}
        closable={false}
        onClose={onClose}
        open={open}
        key={placement}
      >
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
                title: "Stock",
                dataIndex: "stockQuantity",
              },
              {
                title: "Length",
                dataIndex: "length",
              },
              {
                title: "Width",
                dataIndex: "width",
              },
              {
                title: "Height",
                dataIndex: "height",
              },
              {
                title: "Created At",
                dataIndex: ["createdAt"],
                render: (createdAt) =>
                  moment(createdAt).format("YYYY-MM-DD HH:mm:ss"), // Formatea la fecha usando moment
              },
              {
                title: "Updated At",
                dataIndex: ["updatedAt"],
                render: (updatedAt) =>
                  updatedAt
                    ? moment(updatedAt).format("YYYY-MM-DD HH:mm:ss")
                    : "",
              },
              {
                title: "Created By",
                dataIndex: ["createdBy", "email"],
              },
              {
                title: "Updated By",
                dataIndex: ["updatedBy", "email"],
              },
            ]}
            loading={loading}
            pagination={{
              current: pageInfo?.current,
              total: pageInfo?.sizeData,
              onChange: (page) => setPageInfo({ ...pageInfo, current: page }),
            }}
            dataSource={pageInfo.results}
            rowKey={"id"}
          />
        </ConfigProvider>
      </Drawer>
    </>
  );
}
