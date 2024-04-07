import React, { useState } from "react";
import { Input, Button, Modal, Form, Select, Space } from "antd";
import { Category } from "@/src/models/Category.model";
import axios from "axios";
import useAuthStore from "@/src/states/AuthStore";
import {
  NewSearchTerms,
  useSearchTermsProducts,
} from "@/src/states/SearchTermProducts";
const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};
const tailLayout = {
  wrapperCol: { offset: 8, span: 16 },
};
interface SearchBarProps {
  onSearch: () => void;
  onClose: () => void;
}

const SearchFormProducts = ({ onSearch, onClose }: SearchBarProps) => {
  const { productCode, productName, categoryName, addSearchTerms } =
    useSearchTermsProducts(); // Usa el hook de Zustand
  const [form] = Form.useForm();
  const handleSearch = (values: any) => {
    const { code, name, category } = values; // Obtén los valores del formulario
    const newSearchTerms: NewSearchTerms = {
      productCode: code || "", // Usa cadenas vacías si los valores son falsy
      productName: name || "",
      categoryName: category || "",
    };
    addSearchTerms(newSearchTerms); // Llama a la función de búsqueda con los términos actualizados en el estado de Zustand
    onSearch();
  };

  return (
    <Modal
      title="Buscar Productos"
      open={true} // Puedes cambiar esto según tu lógica para mostrar/ocultar el modal
      onCancel={onClose}
      footer={[]}
    >
      <Form
        {...layout}
        form={form}
        onFinish={handleSearch}
        name="control-search-products"
        layout="vertical"
      >
        <Form.Item name="code" label="Código de Producto">
          <Input />
        </Form.Item>
        <Form.Item name="name" label="Nombre del Producto">
          <Input />
        </Form.Item>
        <Form.Item name="category" label="Nombre de la Categoría">
          <Input />
        </Form.Item>
        <Form.Item {...tailLayout}>
          <Space>
            <Button type="primary" htmlType="submit">
              Buscar
            </Button>
          </Space>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default SearchFormProducts;
