import asyncio

import aiohttp
from bs4 import BeautifulSoup
from bs4.element import ResultSet


async def search_web(url: str, tag: str, attrs: dict) -> ResultSet:
    """Search HTML elements at the given URL

    Args:
        url (str): Link to website
        tag (str): HTML tag
        attrs (dict): HTML attributes, which will be contained in HTML element

    Returns:
        ResultSet: List of HTML elements
    """
    async with aiohttp.ClientSession() as session:
        async with session.get(url) as response:
            return BeautifulSoup(await response.text(), "html.parser").find_all(
                tag, attrs=attrs
            )
