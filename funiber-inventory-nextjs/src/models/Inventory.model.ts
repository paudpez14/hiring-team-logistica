import { UserModel } from "./User.model";

export interface InventoryModel {
    id?: number;
    stockQuantity?: number;
    length?: number;
    width?: number;
    height?: number;
    createdAt?: Date;
    updatedAt?: Date;
    createdBy?: UserModel;
    updatedBy?: UserModel;
    isActive?: string;
  }