import EventBus from 'react-native-event-bus';

export const RouteConfig = {
    home: {
        key: 'home',
        title: '首页',
        pathname: '/'
    },
    user: {
        key: 'user',
        title: '用户管理'
        , pathname: '/user'
    },
    category: {
        key: 'category',
        title: '类别列表'
        , pathname: '/category'
    },
    addCategory: {
        key: 'addCategory',
        title: '添加类别'
        , pathname: '/category-add'
    },
    configList: {
        key: 'configList',
        title: '配置列表'
        , pathname: '/config'
    },
    configAdd: {
        key: 'configAdd',
        title: '添加配置'
        , pathname: '/config-add'
    }
};
export const ROUTE_CHANGE = 'routeChange';
export default class NavigationUtil {
    static goto(route, history) {
        const {pathname} = route;
        if (!history || !pathname) {
            console.log('history && pathname cannot be null.')
        }
        //发送导航消息
        EventBus.getInstance().fireEvent(ROUTE_CHANGE, {goto: route});
        return params => {
            history.push({
                pathname,
                ...(params || {})
            })
        }
    }

}
