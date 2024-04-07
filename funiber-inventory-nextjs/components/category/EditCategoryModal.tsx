import React, { useEffect, useState } from "react";
import { Modal, Form, Input, Button, message } from "antd";
import { Category } from "@/src/models/Category.model";
import useAuthStore from "@/src/states/AuthStore";
import axios from "axios";
import { InventoryEditForm } from "@/src/models/InventoryEditForm.model";
import { CategoryEditForm } from "@/src/models/CategoryEditForm.model";
const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};

const tailLayout = {
  wrapperCol: { offset: 8, span: 16 },
};
interface EditCategoryModalProps {
  availableEditCategory: boolean;
  setAvailableEditCategory: (event: boolean) => void;
  category: Category;
  setCompleteEditCategory: (event: boolean) => void;
}

export default function EditCategoryModal({
  availableEditCategory,
  setAvailableEditCategory,
  category,
  setCompleteEditCategory,
}: EditCategoryModalProps) {
  const [form] = Form.useForm();
  const apiUrl = process.env.NEXT_PUBLIC_HOST_FUNIBER_BACKEND || "";
  const apiInventoryPath =
    process.env.NEXT_PUBLIC_API_PATH_INVENTORY_MANAGEMENT || "";
  const [messageApi, contextHolder] = message.useMessage();
  const token = useAuthStore((state) => state.token);
  const user = useAuthStore((state) => state.user);

  const [confirmLoading, setConfirmLoading] = useState(false);
  const handleCancel = () => {
    setAvailableEditCategory(false);
  };

  const onFinish = async (value: CategoryEditForm) => {
    setConfirmLoading(true);
    try {
      const response = await axios.put(
        `${apiUrl}${apiInventoryPath}categories/${category.id}`,
        { code: value.code, name: value.name },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
            Email: user?.email || "",
          },
        }
      );
      if (response.status === 200) {
        messageApi.open({
          type: "success",
          content: "Categoria editada",
          duration: 5,
        });
        setAvailableEditCategory(false);
        setTimeout(() => {
          setConfirmLoading(false);
          setCompleteEditCategory(true);
        }, 1000);
      } else {
        console.error("Error editin category:", response);
        setConfirmLoading(false);
        messageApi.open({
          type: "error",
          content: "No se pudo editar la category",
          duration: 5,
        });
      }
    } catch (error) {
      console.error("There was a problem with your fetch operation:", error);
      setConfirmLoading(false);
      messageApi.open({
        type: "error",
        content: "No se pudo editar la category",
        duration: 5,
      });
    }
  };
  useEffect(() => {
    form.setFieldsValue({
      code: category.code,
      name: category.name,
    });
  }, [category]); // Usar category key para actualizar el efecto
  return (
    <>
      {contextHolder}
      <Modal
        title="Editar Categoría"
        open={availableEditCategory}
        onCancel={handleCancel}
        footer={[]}
        confirmLoading={confirmLoading}
      >
        <Form onFinish={onFinish} form={form} layout="vertical">
          <Form.Item
            name="code"
            label="Código"
            initialValue={category.code}
            rules={[
              { required: true, message: "Ingrese el código de la categoría" },
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="name"
            label="Nombre"
            initialValue={category.name}
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
    </>
  );
}
