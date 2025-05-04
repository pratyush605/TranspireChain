import {useDispatch, useSelector} from 'react-redux';
import {hideAlertMessage} from '../redux/commonSlice';
import { RootState } from '../redux/store';
import "../assets/css/AlertSlide.css"

export default function AlertSlide() {
  const message = useSelector((state: RootState) => state.common.alertMessage);
  const alertType = useSelector((state: RootState) => state.common.alertType);
  const isAlertOpen = useSelector((state: RootState) => state.common.isAlertOpen);
  const dispatch = useDispatch();
  return (
    <div className={`alert-wrapper ${isAlertOpen ? 'alert-show' : 'alert-hide'}`}>
      <div
        className={`d-flex alert alert-${
          alertType === 'error' ? 'danger' : alertType
        } alert-slide`}
      >
        <div dangerouslySetInnerHTML={{__html: `<strong>${message}</strong>`}} />
        <div
          className="cursor-pointer d-flex align-items-center"
          onClick={() => dispatch(hideAlertMessage())}
        >
        </div>
      </div>
    </div>
  );
}
