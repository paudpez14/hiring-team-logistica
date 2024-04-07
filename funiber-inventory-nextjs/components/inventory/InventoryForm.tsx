import React, { useEffect, useState } from "react";
import { Form, InputNumber, Select, Modal, Button, message } from "antd";
import { InventoryForm } from "@/src/models/InventoryForm.model";
import { Product } from "@/src/models/Product.model";
import axios from "axios";
import useAuthStore from "@/src/states/AuthStore";

interface InventoryFormProps {
  availableInventory: boolean;
  setIsAvailableInventory: (value: boolean) => void;
  setIsNewInventory: (value: boolean) => void;
}

const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};

const tailLayout = {
  wrapperCol: { offset: 8, span: 16 },
};

export default function InventoryForm({
  availableInventory,
  setIsAvailableInventory,
  setIsNewInventory,
}: InventoryFormProps) {
  const apiUrl = process.env.NEXT_PUBLIC_HOST_FUNIBER_BACKEND || "";
  const apiInventoryPath =
    process.env.NEXT_PUBLIC_API_PATH_INVENTORY_MANAGEMENT || "";
  const token = useAuthStore((state) => state.token);
  const user = useAuthStore((state) => state.user);
  const [results, setResults] = useState<Product[]>([]);
  const [loading, setLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();
  const [form] = Form.useForm();

  const onFinish = async (values: InventoryForm) => {
    setLoading(true);
    try {
      const response = await axios.post(
        `${apiUrl}${apiInventoryPath}register-inventory`,
        {
          idProduct: Number(values.idProduct),
          stockQuantity: values.stockQuantity,
          length: values.length,
          width: values.width,
          height: values.height,
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
          content: "Inventario nuevo agregado",
          duration: 5,
        });
        setIsAvailableInventory(false);
        setTimeout(() => {
          setLoading(false);
          setIsNewInventory(true);
        }, 1000);
      } else {
        console.error("Error adding inventory:", response.data);
        setLoading(false);
        messageApi.open({
          type: "error",
          content: "No se pudo agregar el inventario",
          duration: 5,
        });
      }
    } catch (error: any) {
      console.error("Error adding inventory:", error);
      setLoading(false);
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

  const fetchProducts = async () => {
    try {
      setLoading(true); // Iniciar la carga de productos
      const response = await axios.get(
        `${apiUrl}${apiInventoryPath}products?type=act`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            Email: user?.email || "",
            "Content-Type": "application/json",
          },
        }
      );
      if (response.status === 200) {
        const mappedResults: Product[] = response.data.data.map(
          (result: Product) => ({
            id: result.id,
            code: result.code,
            name: result.name,
          })
        );
        setResults(mappedResults);
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

  useEffect(() => {
    if (availableInventory) {
      fetchProducts();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [availableInventory]);

  const handleCancel = () => {
    setIsAvailableInventory(false);
  };

  const filterOption = (
    input: string,
    option?: { label: string; value: string }
  ) => (option?.label ?? "").toLowerCase().includes(input.toLowerCase());

  const options = results.map((result) => ({
    label: result.code + " - " + result.name,
    value: result.id.toString(), // Asegúrate de convertir el id a string si es necesario
  }));

  return (
    <>
    {contextHolder}
    <Modal
      title="Agregar Nuevo Inventario"
      open={availableInventory}
      onCancel={handleCancel}
      footer={null}
    >
      <Form {...layout} form={form} onFinish={onFinish}>
        <Form.Item
          name="idProduct"
          label="Producto"
          rules={[
            { required: true, message: "Por favor seleccione el producto" },
          ]}
        >
          <Select
            showSearch
            optionFilterProp="children"
            filterOption={filterOption}
            options={options}
            loading={loading}
            placeholder="Seleccione un producto"
          ></Select>
        </Form.Item>
        <Form.Item
          name="width"
          label="Ancho (Width)"
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
          <Button htmlType="button" onClick={onReset}>
            Reiniciar Formulario
          </Button>
        </Form.Item>
      </Form>
    </Modal>
    </>
  );
}
