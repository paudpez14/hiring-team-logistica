import { CategoryModel } from "./Category.model";
import { InventoryModel } from "./Inventory.model";

export interface ProductResumeModel {
    id?: number,
    code?: string,
    name?: string,
    category?: CategoryModel,
    inventory?: InventoryModel
}