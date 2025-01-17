import ToastUiEditor from '@/components/common/ToastUiEditor/ToastUiEditor';
import { Meta, Story } from '@storybook/react';

export default {
	title: 'Article/ToastUiEditor',
	component: ToastUiEditor,
	decorators: [
		(Story) => (
			<div style={{ width: '320px' }}>
				<Story />
			</div>
		),
	],
} as Meta;

const Template: Story = (args) => <ToastUiEditor {...args} />;

export const DefaultEditor = Template.bind({});
DefaultEditor.args = {};
