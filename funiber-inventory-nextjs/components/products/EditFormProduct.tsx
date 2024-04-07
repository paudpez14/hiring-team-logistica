import { Category } from "@/src/models/Category.model";
import { ProductEditForm } from "@/src/models/ProductEditForm.model";
import { ProductForm } from "@/src/models/ProductForm.model";
import { ProductResume } from "@/src/models/ProductResume.model";
import { Product } from "@/src/models/Product.model";
import { useState } from "react";
import { Button, Modal } from "antd";
import { Form, Input, Select, Space, message } from "antd";
const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};
const tailLayout = {
  wrapperCol: { offset: 8, span: 16 },
};
interface EditFormProductProps {
  product: ProductResume;
  categories: Category[];
  isEditing: boolean;
  setIsEditing: (event: boolean) => void;
  onEdit: (event: ProductEditForm) => void;
  onCancel: (event: React.ChangeEvent<unknown>) => void;
}

export default function EditFormProduct({
  product,
  categories,
  onEdit,
  setIsEditing,
  isEditing,
}: EditFormProductProps) {
  const [editedProduct] = useState<ProductEditForm>({
    id: product.id,
    name: product.name,
    code: product.code,
    category_id: product.category.id,
  });
  const [form] = Form.useForm();
  const [open, setOpen] = useState(isEditing);

  const handleCancel = () => {
    setOpen(false);
    setIsEditing(false);
  };
  const handleEdit = async (values: any) => {
    await onEdit({
      ...product,
      name: values.name,
      code: values.code,
      category_id: values.category,
    });
    setOpen(false);
    setIsEditing(false);
  };

  // Configuración de las opciones del select buscable
  const options = categories.map((category) => ({
    label: category.code + " - " + category.name,
    value: category.id.toString(), // Asegúrate de convertir el id a string si es necesario
  }));
  const filterOption = (
    input: string,
    option?: { label: string; value: string }
  ) => (option?.label ?? "").toLowerCase().includes(input.toLowerCase());
  return (
    <>
      <Modal
        title="Editar Producto"
        open={open}
        onCancel={handleCancel}
        footer={[]}
      >
        <Form
          {...layout}
          form={form}
          name="control-hooks"
          onFinish={handleEdit}
          style={{ maxWidth: 600 }}
        >
          <Form.Item
            name="code"
            label="Código del Producto"
            initialValue={editedProduct.code}
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
            initialValue={editedProduct.name}
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
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}
