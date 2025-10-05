import express from 'express';
import { router } from './routes/index.js';
import { connectDB } from './db/db.js';

export const app = express();
connectDB();

app.use(express.json());

app.use('/api', router);
