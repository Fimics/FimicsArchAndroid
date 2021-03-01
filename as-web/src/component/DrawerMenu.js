import React from 'react';
import {Layout, Menu} from 'antd';
import {withRouter} from 'react-router-dom';
import './DrawerMenu.less';
import {
    HomeOutlined, ProjectOutlined
    , AppstoreAddOutlined
    , TeamOutlined
    , PicCenterOutlined
    , ShopOutlined
    , FileAddFilled
} from '@ant-design/icons'
import NavigationUtil, {RouteConfig as MENUS, ROUTE_CHANGE} from './Navigator';
import EventBus from 'react-native-event-bus';

const {Sider} = Layout;
const {SubMenu} = Menu;

class Index extends React.Component {
    state = {
        selectedKeys: ['home']
    };

    componentDidMount() {
        EventBus.getInstance().addListener(ROUTE_CHANGE, this.listener = route => {
            const {goto: {key, pathname, title}} = route;
            this.setState(
                {
                    selectedKeys: [key]
                }
            );
            const {onMenuSelect} = this.props;
            onMenuSelect && onMenuSelect(pathname, title);
        });
    }

    componentWillMount() {
        EventBus.getInstance().removeListener(this.listener);
    }

    onCollapse = collapsed => {
        this.setState({collapsed});
    };
    onSelect = selectedKeys => {
        const menu = MENUS[selectedKeys.key];
        let pathname = (menu || {}).pathname;
        const {history, onMenuSelect} = this.props;

        if (pathname) {
            NavigationUtil.goto(menu, history)();
            onMenuSelect && onMenuSelect(pathname, menu.title);
        }
    };

    menu() {
        const {selectedKeys} = this.state;
        return <Menu theme='dark'
                     defaultSelectedKeys={selectedKeys}
                     selectedKeys={selectedKeys}
                     mode='inline'
                     onSelect={this.onSelect}
        >
            <Menu.Item key={MENUS.home.key} icon={<HomeOutlined/>}>
                {MENUS.home.title}
            </Menu.Item>
            <SubMenu key='category'
                     title='类别管理'
                     icon={<ProjectOutlined/>}
            >
                <Menu.Item key={MENUS.category.key} icon={<AppstoreAddOutlined/>}>
                    {MENUS.category.title}
                </Menu.Item>
                <Menu.Item key={MENUS.addCategory.key} icon={<PicCenterOutlined/>}>
                    {MENUS.addCategory.title}
                </Menu.Item>
            </SubMenu>
            <Menu.Item key={MENUS.user.key} icon={<TeamOutlined/>}>
                {MENUS.user.title}
            </Menu.Item>
            <SubMenu key='configCenter'
                     title='配置中心'
                     icon={<ProjectOutlined/>}>
                <Menu.Item key={MENUS.configList.key} icon={<ShopOutlined/>}>
                    {MENUS.configList.title}
                </Menu.Item>
                <Menu.Item key={MENUS.configAdd.key} icon={<FileAddFilled/>}>
                    {MENUS.configAdd.title}
                </Menu.Item>
            </SubMenu>
        </Menu>
    }

    render() {
        const {collapsed} = this.props;
        const headerTitle = collapsed ? null : <div className='drawer-header-text-container'>
            <label className='drawer-header-text'>移动端架构师</label>
            <label className='drawer-header-text'>管理后台</label>
        </div>;
        return (
            <Sider trigger={null} collapsed={collapsed} collapsible onCollapse={this.onCollapse}>
                <div className='drawer-header'>
                    <img className='drawer-logo' alt="logo" src='https://www.devio.org/img/avatar.png'/>
                    {headerTitle}
                </div>
                {this.menu()}
            </Sider>
        );
    }

}

//withRouter是react-router的一个高阶组件，获取history
//render时会把match，location和history出入props
export default withRouter(Index);
