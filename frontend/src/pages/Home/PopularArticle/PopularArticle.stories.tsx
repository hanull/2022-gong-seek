import { Meta, Story } from '@storybook/react';
import PopularArticle, { PopularArticlesProps } from '@/pages/Home/PopularArticle/PopularArticle';

export default {
	title: 'pages/home/PopularArticle',
	component: PopularArticle,
	decorators: [
		(Story) => (
			<div style={{ width: '320px' }}>
				<Story />
			</div>
		),
	],
} as Meta;

const Template: Story = (args) => <PopularArticle {...args} />;

export const DefaultCategorySelector = Template.bind({});
DefaultCategorySelector.args = {};
