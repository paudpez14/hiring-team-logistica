export interface PageModel<T> {
    current: number,
    hasNext?: boolean,
    hasPrevious?: boolean,
    numPages?: number,
    sizeData?: number,
    results?: T[]
}