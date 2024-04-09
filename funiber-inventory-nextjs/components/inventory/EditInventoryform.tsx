import { CategoryModel } from "@/src/models/Category.model";
import { InventoryModel } from "@/src/models/Inventory.model";
import { InventoryEditFormModel } from "@/src/models/InventoryEditForm.model";
import { ProductEditFormModel } from "@/src/models/ProductEditForm.model";
import useAuthStore from "@/src/states/AuthStore";
import { Form, message, Modal, Select, Space, Button, InputNumber } from "antd";
import axios from "axios";
import { Input } from "postcss";
import React, { useEffect, useState } from "react";
interface EditFormProductProps {
  inventory: InventoryModel;
  availabeEditing: boolean;
  setAvailableEditing: (event: boolean) => void;
  setIsEditInventory: (event: boolean) => void;
}
const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};

const tailLayout = {
  wrapperCol: { offset: 8, span: 16 },
};

export default function EditInventoryform({
  inventory,
  availabeEditing,
  setAvailableEditing,
  setIsEditInventory,
}: EditFormProductProps) {
  const apiUrl = process.env.NEXT_PUBLIC_HOST_FUNIBER_BACKEND || "";
  const apiInventoryPath =
    process.env.NEXT_PUBLIC_API_PATH_INVENTORY_MANAGEMENT || "";

  const [form] = Form.useForm();
  const [messageApi, contextHolder] = message.useMessage();
  const token = useAuthStore((state) => state.token);
  const user = useAuthStore((state) => state.user);
  const [confirmLoading, setConfirmLoading] = useState(false);
  const handleCancel = () => {
    setAvailableEditing(false);
  };
  const onFinish = async (value: InventoryEditFormModel) => {
    setConfirmLoading(true);
    try {
      const response = await axios.put(
        `${apiUrl}${apiInventoryPath}update-inventory`,
        {
          idInventory: Number(inventory.id),
          stockQuantity: value.stockQuantity,
          length: value.length,
          width: value.width,
          height: value.height,
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
      if (response.status === 200) {
        messageApi.open({
          type: "success",
          content: "Inventario editado",
          duration: 5,
        });
        setAvailableEditing(false);
        setTimeout(() => {
          setConfirmLoading(false);
          setIsEditInventory(true);
        }, 1000);
      } else {
        console.error("Error adding inventory:", response.data);
        setConfirmLoading(false);
        messageApi.open({
          type: "error",
          content: "No se pudo editar el inventario",
          duration: 5,
        });
      }
    } catch (error: any) {
      console.error("Error adding inventory:", error);
      setConfirmLoading(false);
      messageApi.open({
        type: "error",
        content: error.response.data.message,
        duration: 5,
      });
    }
  };
  useEffect(() => {
    form.setFieldsValue({
      stockQuantity: inventory.stockQuantity,
      length: inventory.length,
      width: inventory.width,
      heigth: inventory.height,
    });
  }, [inventory]); // Usar category key para actualizar el efecto
  return (
    <>
      {contextHolder}
      <Modal
        title="Editando Inventario"
        open={availabeEditing}
        onCancel={handleCancel}
        footer={null}
      >
        <Form {...layout} form={form} onFinish={onFinish}>
          <Form.Item
            name="width"
            label="Ancho (Width)"
            initialValue={inventory.width}
            rules={[
              { required: true, message: "Por favor ingrese el ancho" },
              { type: "number", message: "El ancho debe ser un número" },
              {
                pattern: /^\d+(\.\d{1,2})?$/,
                message: "Ingrese un número válido",
              },
            ]}
          >
            <InputNumber
              min={0}
              step={0.01}
              placeholder="Ancho"
              style={{ width: "100%" }}
            />
          </Form.Item>
          <Form.Item
            name="height"
            label="Alto (Height)"
            initialValue={inventory.height}
            rules={[
              { required: true, message: "Por favor ingrese el alto" },
              { type: "number", message: "El alto debe ser un número" },
              {
                pattern: /^\d+(\.\d{1,2})?$/,
                message: "Ingrese un número válido",
              },
            ]}
          >
            <InputNumber
              min={0}
              step={0.01}
              placeholder="Alto"
              style={{ width: "100%" }}
            />
          </Form.Item>
          <Form.Item
            name="length"
            initialValue={inventory.length}
            label="Largo (Length)"
            rules={[
              { required: true, message: "Por favor ingrese el largo" },
              { type: "number", message: "El largo debe ser un número" },
              {
                pattern: /^\d+(\.\d{1,2})?$/,
                message: "Ingrese un número válido",
              },
            ]}
          >
            <InputNumber
              min={0}
              step={0.01}
              placeholder="Largo"
              style={{ width: "100%" }}
            />
          </Form.Item>
          <Form.Item
            name="stockQuantity"
            label="Stock"
            initialValue={inventory.stockQuantity}
            rules={[
              { required: true, message: "Por favor ingrese el stock" },
              { type: "number", message: "El stock debe ser un número" },
              { pattern: /^[+-]?\d+$/, message: "Ingrese un número válido" },
            ]}
          >
            <InputNumber
              min={-999}
              max={999}
              placeholder="Stock"
              style={{ width: "100%" }}
            />
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
