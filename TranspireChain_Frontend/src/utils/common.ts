import {hideAlertMessage, setAlertMessage} from '../redux/commonSlice';
import { AppDispatch } from '../redux/store';

export const showAlert = (message: string, dispatch: AppDispatch, type: string = 'success') => {
    dispatch(setAlertMessage({message, type}));
    setTimeout(() => {
        dispatch(hideAlertMessage());
    }, 5000);
  };