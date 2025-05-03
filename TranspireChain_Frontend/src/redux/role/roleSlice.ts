import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface RoleState {
    token: string | null;
    role: string | null;
}

const initialState: RoleState = {
    token: null,
    role: null,
};

const roleSlice = createSlice({
    name: "roleSlice",
    initialState,
    reducers: {
        handleLogin: (state, action: PayloadAction<{token: string, role: string}>) => {
          state.token = action.payload.token;
          state.role = action.payload.role;
        },
        handleLogout: (state) => {
          state.token = null;
          state.role = null;
        },
    }
});

export const { handleLogin, handleLogout} = roleSlice.actions;

export default roleSlice.reducer;