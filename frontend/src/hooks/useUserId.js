import { useSelector } from 'react-redux';

export const useUserId = () => useSelector(state => state.auth.userId);