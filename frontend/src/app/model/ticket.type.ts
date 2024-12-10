export type Ticket = {
    id: number;
    createdAt: Date;
    expireDate: Date;
    event: string;
    amount: number;
    price: number;
};