import React, { useEffect, useState } from "react";
import { Form, InputNumber, Select, Modal, Button, message, Input } from "antd";
import { InventoryForm } from "@/src/models/InventoryForm.model";
import { Product } from "@/src/models/Product.model";
import axios from "axios";
import useAuthStore from "@/src/states/AuthStore";
import { CategoryForm } from "@/src/models/CategoryForm.model";
const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};

const tailLayout = {
  wrapperCol: { offset: 8, span: 16 },
};
interface NewCategoryModalProps {
  availableNewCategory: boolean;
  setAvailableEditNewCategory: (value: boolean) => void;
  setCompleteNewCategory: (value: boolean) => void;
}
export default function NewCategoryModal({
  availableNewCategory,
  setAvailableEditNewCategory,
  setCompleteNewCategory,
}: NewCategoryModalProps) {
  const apiUrl = process.env.NEXT_PUBLIC_HOST_FUNIBER_BACKEND || "";
  const apiInventoryPath =
    process.env.NEXT_PUBLIC_API_PATH_INVENTORY_MANAGEMENT || "";
  const token = useAuthStore((state) => state.token);
  const user = useAuthStore((state) => state.user);
  const [loading, setLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();
  const [form] = Form.useForm();
  const handleCancel = () => {
    setAvailableEditNewCategory(false);
  };
  const onFinish = async (value: CategoryForm) => {
    setLoading(true);
    try {
      const response = await axios.post(
        `${apiUrl}${apiInventoryPath}categories`,
        {
          code: value.code,
          name: value.name
        },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
            Email: user?.email || "",
          },
        }
      );
      if (response.status === 201) {
        messageApi.open({
          type: "success",
          content: "Categoria nuevo agregado",
          duration: 5,
        });
        setAvailableEditNewCategory(false);
        setTimeout(() => {
          setLoading(false);
          setCompleteNewCategory(true);
        }, 1000);
      } else {
        console.error("Error adding category:", response.data);
        setLoading(false);
        messageApi.open({
          type: "error",
          content: "No se pudo crear la categoria",
          duration: 5,
        });
      }
    } catch (error: any) {
      console.error("Error adding category:", error);
      setLoading(false);
      messageApi.open({
        type: "error",
        content: error.response.data.message,
        duration: 5,
      });
    }
  };
  return (
    <div>
      {contextHolder}
      <Modal
        title="Agregar Categoría"
        open={availableNewCategory}
        onCancel={handleCancel}
        footer={[
        ]}
        confirmLoading={loading}
      >
        <Form form={form} layout="vertical" onFinish={onFinish}>
          <Form.Item
            name="code"
            label="Código"
            rules={[
              { required: true, message: "Ingrese el código de la categoría" },
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="name"
            label="Nombre"
            rules={[
              { required: true, message: "Ingrese el nombre de la categoría" },
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item {...tailLayout}>
          <Button type="primary" htmlType="submit">
            Guardar
          </Button>
        </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
