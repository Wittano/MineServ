export default function redirect(to: string = '/') {
    const endpoint = to == '/' ? '' : to

    window.location.replace(`${window.location.origin}/${endpoint}`)
}