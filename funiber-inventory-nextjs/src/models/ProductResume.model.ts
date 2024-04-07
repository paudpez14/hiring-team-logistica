import { Category } from "./Category.model";
import { Inventory } from "./Inventory.model";

export interface ProductResume {
    id: number,
    code: string,
    name: string,
    category: Category,
    inventory: Inventory
}