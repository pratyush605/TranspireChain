import { configureStore } from "@reduxjs/toolkit";
import roleReducer from "./roleSlice";
import commonReducer from "./commonSlice";

const store = configureStore({
  reducer: {
    auth: roleReducer,
    common: commonReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
