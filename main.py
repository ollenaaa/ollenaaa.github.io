import os
import shutil
import sys
from pathlib import Path

CYRILLIC_SYMBOLS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяєіїґ"
TRANSLATION = ("a", "b", "v", "g", "d", "e", "e", "j", "z", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "y", "", "e", "yu", "ya", "je", "i", "ji", "g")

TRANS = {}

for c, l in zip(CYRILLIC_SYMBOLS, TRANSLATION):
    TRANS[ord(c)] = l
    TRANS[ord(c.upper())] = l.upper()


folders = {
    'images': ('.jpeg', '.png', '.jpg', '.svg'),
    'video': ('.avi', '.mp4', '.mov', '.mkv'),
    'documents': ('.doc', '.docx', '.txt', '.pdf', '.xlsx', '.pptx'),
    'audio': ('.mp3', '.ogg', '.wav', '.amr'),
    'archives': ('.zip', '.gz', '.tar'),
    'unknown': (),
}

categorized_files = {'images': [], 'audio': [], 'video': [], 'documents': [], 'archives': [], 'unknown': []}

known_extensions = []
unknown_extensions = []


def is_directory(path):
    if len(sys.argv) != 2:
        return False
    else:
        try:
            if path.is_dir():
                print(f'{path} is a directory.')
                return True
            else:
                print(f'{path} is not a directory or path is incorrect.')
        except Exception as e:
            print(f'An error occurred: {str(e)}.')
    return False


def normalize(str):
    new_name = ''
    for char in str:
        if char.isalnum():
            new_name += char.translate(TRANS)
        else:
            new_name += '_'
    return new_name


def sort(path):
    os.chdir(path)

    for name in folders.keys():
        if name != 'unknown':
            os.mkdir(name)

    for root, dirs, files in os.walk(path):
        if not 'images' in str(root) and not 'video' in str(root) and not 'documents' in str(root) and not 'audio' in str(root) and not 'archives' in str(root):

            for file in files:

                flag = True
                file_root, file_extension = os.path.splitext(file)

                for folder, extension in folders.items():
                    if file_extension in extension:
                        normalized_file_root = normalize(file_root)
                        new_file_name = f'{normalized_file_root}{file_extension}'
                        known_extensions.append(file_extension)
                        flag = False

                        if folder == 'archives':
                            shutil.unpack_archive(file, f'{normalized_file_root}')
                            categorized_files[folder].append(normalized_file_root)
                            shutil.move(normalized_file_root, f'{folder}/{normalized_file_root}')
                            os.remove(file)

                        elif file != new_file_name:
                            categorized_files[folder].append(new_file_name)
                            shutil.move(file, f'{folder}/{new_file_name}')

                if flag:
                    unknown_extensions.append(file_extension)
                    categorized_files['unknown'].append(file)

            for dir in dirs:
                if dir != 'images' and dir != 'video' and dir != 'archives' and dir != 'audio' and dir != 'documents':
                    try:
                        os.rmdir(dir)
                        #print(f'Directory {dir} has been removed successfully')
                    except OSError as error:
                        print(f'Directory {dir} can not be removed')

                    if os.path.exists(dir):
                        normalized_dir = normalize(dir)
                        os.rename(dir, normalized_dir)
                        sort(os.path.join(root, normalized_dir))



if __name__ == '__main__':
    path = Path(sys.argv[1])
    flag = is_directory(path)
    if not flag:
        sys.exit(1)

    sort(path)

    for type, files in categorized_files.items():
        print(f'Список файлів в категорії {type} : {files}')

    print(f'Перелік усіх відомих скрипту розширень, які зустрічаються в цільовій папці: {known_extensions}')
    print(f'Перелік всіх розширень, які скрипту невідомі: {unknown_extensions}')