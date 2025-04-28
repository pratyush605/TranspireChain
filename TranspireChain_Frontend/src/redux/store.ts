import { configureStore } from "@reduxjs/toolkit";
import roleReducer from "./role/roleSlice"; 

const store = configureStore({
  reducer: {
    auth: roleReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
