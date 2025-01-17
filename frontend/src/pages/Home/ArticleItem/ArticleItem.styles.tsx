import { AiOutlineHeart, AiOutlineMessage } from 'react-icons/ai';

import styled from '@emotion/styled';

export const ArticleContent = styled.div`
	display: flex;

	flex-direction: column;
	justify-content: space-between;

	width: 80%;
	height: 100%;

	border-radius: ${({ theme }) => theme.size.SIZE_010};

	padding: ${({ theme }) => theme.size.SIZE_010};

	z-index: ${({ theme }) => theme.zIndex.ARTICLE_POPULAR_CONTENT};
`;

export const Title = styled.h2`
	width: 100%;
	height: ${({ theme }) => theme.size.SIZE_040};

	font-size: ${({ theme }) => theme.size.SIZE_014};
	line-height: normal;
	text-overflow: ellipsis;
	white-space: nowrap;
	overflow: hidden;

	margin-top: ${({ theme }) => theme.size.SIZE_004};
`;

export const ArticleInfo = styled.div`
	display: flex;

	justify-content: space-between;
`;

export const ProfileBox = styled.div`
	display: flex;

	align-items: center;
	gap: ${({ theme }) => theme.size.SIZE_006};
`;

export const UserImg = styled.img`
	width: ${({ theme }) => theme.size.SIZE_024};
	height: ${({ theme }) => theme.size.SIZE_024};

	border-radius: 50%;

	object-fit: cover;
	object-position: center;
`;

export const UserName = styled.span`
	width: ${({ theme }) => theme.size.SIZE_100};
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: normal;

	font-size: ${({ theme }) => theme.size.SIZE_012};

	color: ${({ theme }) => theme.colors.BLACK_600};
`;

export const SubInfoBox = styled.div`
	display: flex;
	margin-top: ${({ theme }) => theme.size.SIZE_004};

	gap: ${({ theme }) => theme.size.SIZE_010};
`;

export const ViewsBox = styled.div`
	display: flex;

	align-items: center;
	gap: ${({ theme }) => theme.size.SIZE_006};
`;

export const ViewsIcon = styled.div`
	font-size: ${({ theme }) => theme.size.SIZE_012};

	color: ${({ theme }) => theme.colors.BLACK_600};
`;

export const ViewsCount = styled.div`
	font-size: ${({ theme }) => theme.size.SIZE_012};

	color: ${({ theme }) => theme.colors.BLACK_600};
`;

export const LikeBox = styled.div`
	display: flex;

	align-items: center;
	gap: ${({ theme }) => theme.size.SIZE_006};
`;

export const LikeIcon = styled(AiOutlineHeart)`
	font-size: ${({ theme }) => theme.size.SIZE_016};

	color: ${({ theme }) => theme.colors.BLACK_600};
`;

export const LikeCount = styled.div`
	font-size: ${({ theme }) => theme.size.SIZE_012};

	color: ${({ theme }) => theme.colors.BLACK_600};
`;

export const CommentBox = styled.div`
	display: flex;

	align-items: center;
	gap: ${({ theme }) => theme.size.SIZE_006};
`;

export const CommentCount = styled.span`
	font-size: ${({ theme }) => theme.size.SIZE_012};

	color: ${({ theme }) => theme.colors.BLACK_600};
`;

export const CommentIcon = styled(AiOutlineMessage)`
	font-size: ${({ theme }) => theme.size.SIZE_016};

	color: ${({ theme }) => theme.colors.BLACK_600};
`;
