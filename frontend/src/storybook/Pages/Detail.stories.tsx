import { PropsWithOptionalChildren } from 'gongseek-types';

import Detail, { DetailProps } from '@/pages/Detail/index';
import Vote from '@/pages/Discussion/Vote/Vote';
import { Meta, Story } from '@storybook/react';

export default {
	title: 'pages/Detail',
	component: Detail,
	decorators: [
		(Story) => (
			<div style={{ width: '320px' }}>
				<Story />
			</div>
		),
	],
} as Meta;

const Template: Story<PropsWithOptionalChildren<DetailProps>> = (args) => <Detail {...args} />;

export const ErrorDetail = Template.bind({});

export const DiscussionDetail = Template.bind({});
DiscussionDetail.args = {
	children: <Vote articleId="4" />,
};
