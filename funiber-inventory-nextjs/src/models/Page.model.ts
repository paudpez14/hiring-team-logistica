export interface Page<T> {
    current: number,
    hasNext?: boolean,
    hasPrevious?: boolean,
    numPages?: number,
    sizeData?: number,
    results?: T[]
}