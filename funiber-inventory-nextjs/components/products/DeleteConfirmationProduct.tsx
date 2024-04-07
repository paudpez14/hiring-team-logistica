import { Product } from "@/src/models/Product.model";
import { Button, Modal } from "antd";

interface DeleteConfirmationProductProps {
  product: Product;
  open: boolean;
  onClose: (event: React.ChangeEvent<unknown>) => void;
  onConfirm: (event: Product) => void;
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
