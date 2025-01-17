import { rest } from 'msw';

import { TVote } from '@/api/vote';
import { HOME_URL } from '@/constants/apiUrl';

export const VoteHandler = [
	rest.post<{ items: string[] }>(`${HOME_URL}/api/articles/:articleId/votes`, (req, res, ctx) => {
		const data = localStorage.getItem('mock-votes');
		const mockVotes = data ? (JSON.parse(data) as TVote[]) : [];

		const { articleId } = req.params;
		const { items } = req.body;

		if (typeof articleId !== 'string') {
			return;
		}

		localStorage.setItem(
			'mock-votes',
			JSON.stringify(
				mockVotes.concat({
					articleId,
					voteItems: items.map((item, idx) => ({
						id: idx,
						content: item,
						amount: 0,
					})),
					votedItemId: null,
					isExpired: false,
				}),
			),
		);

		return res(
			ctx.status(201),
			ctx.json({
				articleId,
			}),
		);
	}),

	rest.get(`${HOME_URL}/api/articles/:articleId/votes`, (req, res, ctx) => {
		const { articleId } = req.params;
		const data = localStorage.getItem('mock-votes');
		const mockVotes = data ? (JSON.parse(data) as TVote[]) : [];

		const vote = mockVotes.find((mockVote) => mockVote.articleId === articleId);
		if (typeof vote === 'undefined') {
			throw new Error('투표를 찾지 못하였습니다');
		}

		return res(ctx.status(200), ctx.json(vote));
	}),

	rest.post<{ votedItemId: string }>(
		`${HOME_URL}/api/articles/:articleId/votes/do`,
		(req, res, ctx) => {
			const data = localStorage.getItem('mock-votes');
			const mockVotes = data ? (JSON.parse(data) as TVote[]) : [];

			const { articleId } = req.params;
			const { votedItemId } = req.body;

			const vote = mockVotes.find((mockVote) => mockVote.articleId === articleId);
			if (typeof vote === 'undefined') {
				throw new Error('투표를 찾지 못하였습니다');
			}

			const votedItem = vote.voteItems.find((voteItem) => String(voteItem.id) === votedItemId);
			if (typeof votedItem === 'undefined') {
				throw new Error('투표 항목을 찾을수 없습니다.');
			}
			votedItem.amount += 1;

			localStorage.setItem('mock-votes', JSON.stringify(mockVotes));

			return res(ctx.status(201));
		},
	),
];
