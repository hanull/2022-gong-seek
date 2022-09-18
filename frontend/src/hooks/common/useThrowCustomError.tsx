import { AxiosError } from 'axios';
import { useEffect } from 'react';

import CustomError from '@/components/helper/CustomError';
import { ErrorMessage } from '@/constants/ErrorMessage';

const useThrowCustomError = (
	error: AxiosError<{
		errorCode: keyof typeof ErrorMessage;
		message: string;
	}> | null,
) => {
	useEffect(() => {
		if (error) {
			if (!error.response) {
				return;
			}
			throw new CustomError(
				error.response.data.errorCode,
				ErrorMessage[error.response.data.errorCode],
			);
		}
	}, [error]);
};

export default useThrowCustomError;
