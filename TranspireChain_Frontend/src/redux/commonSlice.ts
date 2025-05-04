import {createSlice} from '@reduxjs/toolkit';

const commonSlice = createSlice({
  name: 'common',
  initialState: {
    alertMessage: '',
    alertType: 'success',
    continueTo: '/',
    isAlertOpen: false,
    isSearchOpen: false
  },
  reducers: {
    setAlertMessage: (state, param) => {
      const {payload} = param;
      state.alertMessage = payload.message;
      state.alertType = payload.type || 'success';
      state.isAlertOpen = true;
    },
    hideAlertMessage: (state) => {
      state.isAlertOpen = false;
    },
    toggleSearch: (state) => {
      state.isSearchOpen = !state.isSearchOpen;
    },
    setContinueTo: (state, param) => {
      const {payload} = param;
      state.continueTo = payload;
    }
  }
});

const {actions, reducer} = commonSlice;
export const {setAlertMessage, hideAlertMessage, toggleSearch, setContinueTo} = actions;
export default reducer;
