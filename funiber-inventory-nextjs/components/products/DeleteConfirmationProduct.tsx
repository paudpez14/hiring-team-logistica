import { ProductModel } from "@/src/models/Product.model";
import { Button, Modal } from "antd";

interface DeleteConfirmationProductProps {
  product: ProductModel;
  open: boolean;
  onClose: (event: React.ChangeEvent<unknown>) => void;
  onConfirm: (event: ProductModel) => void;
}

export default function DeleteConfirmationProduct({
  product,
  open,
  onClose,
  onConfirm,
}: DeleteConfirmationProductProps) {
  return (
    <Modal
      open={open}
      title="¿Estás seguro de que quieres eliminar este producto?"
      onOk={() => {
        onConfirm(product);
      }}
      onCancel={onClose}
      footer={[
        <Button key="back" onClick={onClose}>
          Cancelar
        </Button>,
        <Button
          key="submit"
          type="primary"
          onClick={() => {
            onConfirm(product);
          }}
        >
          Proceder
        </Button>,
      ]}
    ></Modal>
  );
}
