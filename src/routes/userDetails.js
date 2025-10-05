import { Router } from "express";
import { setUserDetails,getUserDetails } from "../controllers/userDetailsController.js";

export const router = Router();

router.post("/save", setUserDetails);
router.get("/get/:username", getUserDetails);
