import { Router } from "express";
import { router as userRouter } from "./userDetails.js";

export const router = Router();

router.use("/user", userRouter);
