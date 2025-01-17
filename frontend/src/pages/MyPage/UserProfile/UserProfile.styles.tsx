import { AiFillEdit } from 'react-icons/ai';

import Input from '@/components/common/Input/Input';
import styled from '@emotion/styled';

export const Container = styled.div`
	display: flex;

	width: 100%;

	flex-direction: column;
	justify-content: center;
	align-items: center;

	gap: ${({ theme }) => theme.size.SIZE_020};
`;

export const UserProfile = styled.img`
	width: ${({ theme }) => theme.size.SIZE_050};
	height: ${({ theme }) => theme.size.SIZE_050};

	border-radius: 50%;

	object-fit: cover;
	object-position: center;
`;

export const UserName = styled.div`
	text-align: center;
	font-size: ${({ theme }) => theme.size.SIZE_014};
`;

export const UserNameBox = styled.div`
	display: flex;
	gap: ${({ theme }) => theme.size.SIZE_016};

	align-items: center;
`;

export const UserNameContainer = styled.div`
	display: flex;
	flex-direction: column;
	gap: ${({ theme }) => theme.size.SIZE_012};
`;

export const EditIcon = styled(AiFillEdit)`
	cursor: pointer;

	:hover,
	:active {
		color: ${({ theme }) => theme.colors.PURPLE_400};
	}
`;

export const ConfirmButton = styled.button<{ disabled: boolean }>`
	cursor: pointer;

	border-radius: ${({ theme }) => theme.size.SIZE_008};
	border-color: transparent;
	color: ${({ theme }) => theme.colors.WHITE};

	background-color: ${({ disabled, theme }) =>
		disabled ? theme.colors.GRAY_500 : theme.colors.PURPLE_500};
	pointer-events: ${({ disabled }) => disabled && 'none'};
	font-size: ${({ theme }) => theme.size.SIZE_010};
	padding: ${({ theme }) => theme.size.SIZE_006};
	:hover,
	:active {
		background-color: ${({ theme }) => theme.colors.PURPLE_400};
	}
`;

export const EditUserNameInput = styled(Input)`
	height: ${({ theme }) => theme.size.SIZE_016};
`;

export const ValidateMessage = styled.div<{ isValid: boolean }>`
	color: ${({ isValid, theme }) => (isValid ? theme.colors.BLUE_500 : theme.colors.RED_500)};
	font-size: ${({ theme }) => theme.size.SIZE_012};
`;

export const EditUserNameBox = styled.div`
	display: flex;

	gap: ${({ theme }) => theme.size.SIZE_016};
`;
