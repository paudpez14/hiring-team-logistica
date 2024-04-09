import React, { useEffect, useState } from "react";
import { Button, Modal } from "antd";
import { Form, Input, Select, Space, message } from "antd";
import { CategoryModel } from "@/src/models/Category.model";
import axios from "axios";
import useAuthStore from "@/src/states/AuthStore";
const apiUrl = process.env.NEXT_PUBLIC_HOST_FUNIBER_BACKEND || "";
const apiInventoryPath =
  process.env.NEXT_PUBLIC_API_PATH_INVENTORY_MANAGEMENT || "";
const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};
const tailLayout = {
  wrapperCol: { offset: 8, span: 16 },
};
interface ModalAddFormProductProps {
  categories: CategoryModel[];
  isAddProduct: boolean;
  setIsNewProduct: (value: boolean) => void;
  setIsAddProduct: (value: boolean) => void;
}
export default function ModalAddFormProduct({
  categories,
  isAddProduct,
  setIsAddProduct,
  setIsNewProduct,
}: ModalAddFormProductProps) {
  const [messageApi, contextHolder] = message.useMessage();
  const token = useAuthStore((state) => state.token);
  const user = useAuthStore((state) => state.user);
  const [open, setOpen] = useState(isAddProduct);
  const [form] = Form.useForm();
  const [confirmLoading, setConfirmLoading] = useState(false);
  const handleCancel = () => {
    setIsAddProduct(false);
  };
  const onFinish = async (values: any) => {
    setConfirmLoading(true);
    try {
      const response = await axios.post(
        `${apiUrl}${apiInventoryPath}products`,
        {
          code: values.code,
          name: values.name,
          category_id: values.category,
          createdBy: user?.email,
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
          content: "Producto nuevo agregado",
          duration: 5,
        });
        setTimeout(() => {
            setIsNewProduct(true);
            setOpen(false);
          }, 2000);
      } else {
        console.error("Error adding product:", response.data);
        messageApi.open({
          type: "error",
          content: "No se pudo agregar el producto",
          duration: 5,
        });
      }
    } catch (error: any) {
      console.error("Error adding product:", error);
      messageApi.open({
        type: "error",
        content: error.response.data.message,
        duration: 5,
      });
    }
  };
  const onReset = () => {
    form.resetFields();
  };
  const options = categories.map((category) => ({
    label: category.code + " - " + category.name,
    value: category.id !== undefined ? category.id.toString() : '', // Verificación de undefined y conversión a string
  }));
  


  const filterOption = (
    input: string,
    option?: { label: string; value: string }
  ) => (option?.label ?? "").toLowerCase().includes(input.toLowerCase());

  return (
    <>
      {contextHolder}
      <Modal
        title="Agregar Nuevo Producto"
        open={open}
        onCancel={handleCancel}
        confirmLoading={confirmLoading}
        footer={[]}
      >
        <Form
          {...layout}
          form={form}
          name="control-hooks"
          onFinish={onFinish}
          style={{ maxWidth: 600 }}
        >
          <Form.Item
            name="code"
            label="Código del Producto"
            rules={[
              {
                required: true,
                message: "Por favor ingresa el código del producto",
              },
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="name"
            label="Nombre del Producto"
            rules={[
              {
                required: true,
                message: "Por favor ingresa el nombre del producto",
              },
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="category"
            label="Categoría"
            rules={[
              { required: true, message: "Por favor selecciona una categoría" },
            ]}
          >
            <Select
              showSearch
              placeholder="Selecciona una categoría"
              optionFilterProp="children"
              filterOption={filterOption}
              options={options}
            />
          </Form.Item>
          <Form.Item {...tailLayout}>
            <Space>
              <Button type="primary" htmlType="submit">
                Guardar
              </Button>
              <Button htmlType="button" onClick={onReset}>
                Reiniciar Formulario
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}
